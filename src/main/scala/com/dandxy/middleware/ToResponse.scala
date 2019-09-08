package com.dandxy.middleware

import cats.data.{ Kleisli, OptionT }
import cats.instances.stream._
import cats.instances.option._
import cats.syntax.all._
import cats.{ ~>, Applicative, Contravariant, FlatMap, Functor, Monad, MonoidK }
import com.dandxy.middleware.ToResponse.{ apply, instance }
import com.dandxy.middleware.model.Exported

import scala.language.higherKinds

trait ToResponse[F[_], M, B, A] { self =>

  def run: Kleisli[OptionT[F, ?], (M, A), B]

  def run(media: M)(value: A): OptionT[F, B] = run((media, value))

  def dimap[C, D](f: C => A)(g: B => D)(implicit F: Functor[F]): ToResponse[F, M, D, C] =
    instance((m, a) => self.run((m, f(a))).map(g))

  def map[C](f: B => C)(implicit F: Functor[F]): ToResponse[F, M, C, A] =
    apply(self.run.map(f))

  def mapK[G[_]](f: F ~> G): ToResponse[G, M, B, A] =
    apply(self.run.mapF(_.mapK(f)))

  def flatMap[C](f: B => ToResponse[F, M, C, A])(implicit F: Monad[F]): ToResponse[F, M, C, A] =
    apply(self.run.flatMap(f(_).run))

  def andThen[C](f: B => F[C])(implicit F: Monad[F]): ToResponse[F, M, C, A] =
    apply(self.run.andThen(f.andThen(OptionT.liftF(_)(F))))

  def contramap[C](f: C => A): ToResponse[F, M, B, C] =
    instance((m, a) => self.run((m, f(a))))

}

trait LowPrioImplicits {
  implicit def exportedToResponse[F[_], M, B, A](implicit exported: Exported[ToResponse[F, M, B, A]]): ToResponse[F, M, B, A] =
    exported.instance
}

trait ToResponseInstances extends LowPrioImplicits {
  implicit def flatMapResponse[F[_]: FlatMap, M, B, A](implicit encoder: ToResponse[F, M, B, A]): ToResponse[F, M, B, F[A]] =
    ToResponse.instanceF((media, value) => value.flatMap(encoder.run(media)(_).value))

  implicit def eitherResponse[F[_], M, B, L, R](
    implicit l: ToResponse[F, B, M, L],
    r: ToResponse[F, B, M, R]
  ): ToResponse[F, B, M, Either[L, R]] =
    ToResponse.instance((media, value) => value.fold(l.run(media), r.run(media)))
}

object ToResponse extends ToResponseInstances {

  def apply[F[_], M, B, A](f: Kleisli[OptionT[F, ?], (M, A), B]): ToResponse[F, M, B, A] =
    new ToResponse[F, M, B, A] {
      override val run: Kleisli[OptionT[F, ?], (M, A), B] = f
    }

  def instance[F[_], M, B, A](f: (M, A) => OptionT[F, B]): ToResponse[F, M, B, A] =
    apply(Kleisli(Function.tupled(f)))

  def instanceF[F[_], M, B, A](f: (M, A) => F[Option[B]]): ToResponse[F, M, B, A] =
    apply(Kleisli(Function.tupled(f) andThen OptionT.apply))

  def always[F[_]: Functor, M, B, A](f: (M, A) => F[B]): ToResponse[F, M, B, A] =
    apply(Kleisli(Function.tupled(f) andThen OptionT.liftF[F, B]))

  def liftF[F[_], M, B, A](f: F[B])(implicit F: Functor[F]): ToResponse[F, M, B, A] =
    ToResponse(Kleisli.liftF(OptionT.liftF(f)))

  def pure[F[_], M, B, A](value: B)(implicit F: Monad[F]): ToResponse[F, M, B, A] =
    apply(Kleisli.pure(value))

  def empty[F[_], M, B, A](implicit F: Applicative[F]): ToResponse[F, M, B, A] =
    instance((_, _) => OptionT.none)

  def matching[F[_], MS <: Traversable[M], M, B, A](
    fn: A => PartialFunction[M, F[B]]
  )(implicit F: Applicative[F]): ToResponse[F, MS, B, A] =
    instanceF { (media, value) =>
      val f = fn(value)
      media
        .toStream
        .map(f.lift)
        .foldK
        .sequence
    }

  implicit def monoidKInstance[F[_], M, R](
    implicit F: Monad[F]
  ): MonoidK[ToResponse[F, M, R, ?]] with Contravariant[ToResponse[F, M, R, ?]] =
    new MonoidK[ToResponse[F, M, R, ?]] with Contravariant[ToResponse[F, M, R, ?]] {

      override def contramap[A, B](fa: ToResponse[F, M, R, A])(f: B => A): ToResponse[F, M, R, B] =
        fa.contramap(f)

      override def empty[A]: ToResponse[F, M, R, A] = ToResponse.empty[F, M, R, A]

      override def combineK[A](x: ToResponse[F, M, R, A], y: ToResponse[F, M, R, A]): ToResponse[F, M, R, A] =
        x.combineK(y)

    }

  implicit def monadInstance[F[_], M, I](implicit F: Monad[F]): Monad[ToResponse[F, M, ?, I]] =
    new Monad[ToResponse[F, M, ?, I]] {

      implicit val M: Monad[Kleisli[OptionT[F, ?], (M, I), ?]] = Monad[Kleisli[OptionT[F, ?], (M, I), ?]]

      override def flatMap[A, B](fa: ToResponse[F, M, A, I])(f: A => ToResponse[F, M, B, I]): ToResponse[F, M, B, I] =
        fa.flatMap(f)

      override def tailRecM[A, B](a: A)(f: A => ToResponse[F, M, Either[A, B], I]): ToResponse[F, M, B, I] =
        apply(M.tailRecM(a)(f(_).run))

      override def pure[A](x: A): ToResponse[F, M, A, I] = apply(M.pure(x))

    }
}

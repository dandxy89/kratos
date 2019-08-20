

# Interpolating data for PGA Stats
library(clipr)
library(dplyr)
library(magrittr)

# Get the table from the clipboard
clipboardData <- clipr::read_clip_tbl()
table <- dplyr::as_tibble(clipboardData)

# Check we've got the correct data
head(table)

# Basic Plot to eyes on the data
plot(table$Distance, table$Shots)

# Range from Min to Max
rng <- min(table$Distance):max(table$Distance)

# Fit a spline over the data
f <- splinefun(table$Distance, table$Shots)

# Apply the spline function to the data
prediction <- f(rng)

# Compare the correlation between the spline and the actual data
rsq <- function(x, y) summary(lm(y~x))$r.squared
rsq(table$Shots, f(table$Distance))

# Plot for confirmation
plot(table$Distance, table$Shots, type = "l", col = "red")
plot(rng, prediction, type = "l", col = "green")

# Generate output file
output <- data.frame('Distance' = rng, 'Strokes' = prediction)

# Generate the new columns
output <- output %>%
  mutate(Distance = as.integer(Distance)) %>%
  mutate(sql_input = sprintf("   (%1.0f, %f),", Distance, Strokes))

# Write the column to Clipboard
write_clip(output$sql_input)

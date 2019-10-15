# Interpolating data for PGA Stats
library(clipr)
library(dplyr)
library(magrittr)

# Get the table from the clipboard
clipboardData <- clipr::read_clip_tbl()
table <- dplyr::as_tibble(clipboardData)

# Check we've got the correct data
head(table)

# Prepare the values to predict and Data Frame to store them in
output <- dplyr::data_frame(Distance = min(table$distance):max(table$distance))
prediction_values <- output %>% select(Distance) %>% pull()

# Iterate through the table columns
for (name in colnames(table)[2:length(colnames(table))]) {
  # Fit a cubic spline
  f <- splinefun(table %>% select(distance) %>% pull(), 
                 table %>% select(name) %>% pull())
  
  # Update the column
  output <- output %>% mutate(!!name := f(prediction_values))
}

# Demo Output table
head(output)

# Generate the new columns (Update for each)
output <- output %>% 
  mutate(Distance = as.integer(Distance)) %>%
  mutate(sql_input = sprintf("   (%1.0f, %f, %f, %f, %f),", Distance, A, B, C, D))

# Write the column to Clipboars
write_clip(output$sql_input)

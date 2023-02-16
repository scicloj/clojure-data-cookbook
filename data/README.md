
`nycflights13.flights.csv.gz` was generated in R as follows

```{r}
library(tidyverse)
library(nycflights13)
write_csv(flights, "nycflights13.flights.csv.gz")
```

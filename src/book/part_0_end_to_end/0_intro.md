https://www.thedatazone.ca/Assessment/Parcel-Land-Sizes/wg22-3ric/data_preview
https://www.thedatazone.ca/Assessment/Residential-Dwelling-Characteristics/a859-xvcs/data_preview
https://www.thedatazone.ca/Assessment/Parcel-Sales-History/6a95-ppg4/data_preview
https://www.thedatazone.ca/browse?&page=1
https://www.pvsc.ca/data-disclosure
https://www.pvsc.ca/search/node?keys=sales%20data

::: {.callout-warning}
This is an excerpt from the work-in-progress Clojure Data Cookbook. It is currently unstable, under active development, and likely to change in the coming months.
:::

# End to End

To get started, this section of the book works through an example problem (predicting house prices) to give you a high-level idea of what Clojure's data science ecosystem can do. We'll touch on every aspect of a "real" data science problem, including importing, transforming, modelling, and visualizing the data, then generating artifacts and deploying them so they're accessible to people who don't have your computer. This section is necessarily concise, but every topic that's raised is elaborated upon in later chapters. The goal is to get you up and running as fast as possible and give you an idea of what working with data in Clojure is like. All the gory details and gotchas come later 😄.

This section assumes you've already got a Clojure environment set up and can run Clojure code both in a REPL and in a notebook. For details about how to set that all up, see (TODO: chapter here??).

Chapter 1 (@sec-data-import-example) starts with getting data into your notebook and doing some basic cleanup. It sounds like a simple task, and this case it is since our example data is all CSV. But as soon as you start doing data science in the "real world", you run into all kinds of interesting file formats that take some specialized code to read and write. In @sec-data-import we'll go to great lengths to learn how to import every compressed, columnar, scientific, and magical file format you can imagine. For now this example chapter is lean in comparison, but enough to get started.

Chapter 2 (@sec-data-transformation-example) will cover how to transform data. We'll be working with 3 separate datasets in order to cobble together the information we want, and not all of it is well organized or relevant. Real world data is invariably a total mess. In this chapter we'll explore some basic techniques for cleaning it up. We'll cover things like joining datasets together, filling in missing values, de-duplicating rows, (TODO: elaborate..)

Chapter 3 covers modelling our data. Our goal for this exercise is to estimate the value of a house given sales data from similar properties. To do this, we'll explore different approaches to modelling the data and build an algorithm that can predict the price of house and say something about that prediction.

## Basic toolkit

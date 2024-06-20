# Introduction {.unnumbered}

Most people have some sort of data from which they want to extract insights. In the context of this book, by "data science" I just mean working with data broadly speaking. In this sense, anyone with data to investigate who can extract some meaning from it is a data scientist. It's an inherently interdisciplinary field, combining statistics, mathematics, software engineering, and domain expertise. The amount of data we have to deal with daily is only growing, and tools and techniques required to wrangle, analyze, and model this data are quickly evolving to keep up with modern demands on what we expect to be able to find in our data.

The goal of this book is to provide a reference for anyone who has data to work with and an interest in doing it in Clojure. It documents the current community recommendations and default stack for doing data science in Clojure.


Clojure's data science stack is relatively new and in varying states of stability. The foundational elements like high-performance numeric computing and columnar data processing are rock solid, but the farther away we get from raw number crunching, broadly speaking, the less mature the libraries are. This isn't a reason not to use them. Clojure has a culture of stability and very intentionally evolving APIs. It is highly likely that the current ways of doing things will be supported indefinitely (although of course I'm in no position to guarantee it), but it also highly likely that improved approaches will be released over the coming years.

In this book, we'll explore the Clojure data ecosystem and its rich collection of libraries for working with data

## Prerequisites

### Clojure
TODO

### Clojure IDE
TODO

### Project setup
TODO

All of the example files used in this project are available in the `data` directory.

## Prior Art

There have been many attempts to put together a resource like this one in the past. Some reached completion but have since become obsolete, some were never finished, and some of them form the basis for this current project. Below is a list and brief discussion of some other resources you may come across in your journey trying to learn about doing data science with Clojure.

## Other Resources

The resources mentioned in the previous section are now out of date, i.e. no longer referencing current best practices or the latest tools available. There are other resources available, though. Below is a list that is current as of this writing.

## Colophon

This book was written in [Spacemacs](https://www.spacemacs.org) as a collection of markdown and Clojure files. The source code is available on [GitHub here](https://github.com/scicloj/clojure-data-cookbook). The [website](https://clojuredatacookbook.com) is a [Quarto book](https://quarto.org/docs/books/), generated from the source markdown and Clojure files using [Clay](https://github.com/scicloj/clay). It is [manually published](https://quarto.org/docs/publishing/github-pages.html) to [Github Pages](https://pages.github.com) using the Quarto CLI.

This version of the book was built with Clojure version ____ and the following packages:

e.g. https://ggplot2-book.org/introduction#colophon

____ packages______

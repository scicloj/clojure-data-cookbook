^{:kindly/hide-code true}
(ns index)

;; # Welcome {.unnumbered}

;; ::: {.callout-note}
;; **Status:** This is a work in progress and still has many missing and moving parts. Updates are published here as they come and this banner will be removed once this book is "stable" (or at least as stable as it will get until more progress is made in the broader Clojure data science ecosystem). For now this a "building in public" situation -- the "book" is nowhere near complete and not necessarily correct. Browse at your own risk.
;; :::
;;
;; Welcome to the Clojure Data Cookbook! This book will teach you how to work with data in Clojure. You'll learn how to get your data into Clojure, transform it, and visualize it.
;;
;; Approaching a new Clojure project can be intimidating. It's not always obvious where to start. It's not even clear which libraries you'll need, or which ones do what. The Clojure community in general tends to favour bespoke, minimalist configurations that are unique for every project. There are good reasons for this. It tends to lead to more maintainable, more stable code, in the long run. But it does have the major downside of making it harder to get started.
;;
;; Clojure and its ecosystem are very much optimized for long-term, professional use, which makes it a great fit for mission-critical projects with no room to compromise on performance or security, but this lack of frameworks and batteries-included solutions can make it feel intimidating for newcomers.
;;
;; Clojure's story for data science is no different. In recent years a lot of amazing work has been done by the community that makes Clojure a great choice for many data-focused tasks, but it's not always obvious where to start.
;;
;; **This book is a guide through Clojure's ecosystem for data science.** Clojure has come a long way in recent years. Some parts of the data science stack are reaching maturity and need more guides and documentation to help people learn how to use them. Other parts are still very much under active development, notably the data viz story. I believe it's worth getting started learning the fundamentals of how to work with data in Clojure given the stability and maturity of the foundational elements of the ecosystem, despite the ongoing development in higher-level areas, like visualization.

;; ## Goals

;; The primary goal of this book is to help anyone find meaning and insights in data using Clojure's data science toolkit. There are also many side quests I hope will be fulfilled in writing it:
;; - Better documenting Clojure's emerging data science stack
;; - Documenting where the various components of the ecosystem are at and identifying missing pieces (i.e. key areas for future development)
;; - Bringing awareness to Clojure's strengths working with data compared to current mainstream data science tools
;;
;; The book includes some instructions and suggestions for getting a Clojure environment set up, a brief overview of the most relevant parts of the Clojure language itself, and most importantly, a comprehensive collection of example-based explanations of which libraries to use and how to use them to accomplish common tasks in data science using Clojure. We'll cover data input and output, manipulation, analysis, and finally visualisation. The examples include a brief introduction to the type of problem we're solving, the solution itself, and in most cases an explanation of what the solution is doing. The goal is to provide insight into how to think like a Clojurian and solve data problems "the Clojure way".
;;
;; This book is written as a series of Clojure notebooks. I'll explain what those are as we get started, but the short version is that this book itself _is_ code. You can clone this repo and run the examples on your own computer. You can edit and play around with them yourself. This is one of the coolest things about Clojure. In Clojure, code is data and data is code. There is no distinction. This way of thinking about code makes it easy to transform Clojure code into all kinds of different formats. Clojure is most often used to write executable computer programs, but it can just as well be used to generate a presentation, or a website, or a book!
;;
;; It's no secret that I'm a fan of Clojure. Hopefully you will be too by the end of this book. Happy reading :)

;; ## Support
;;
;; This website is and will always be free, licensed under the [CC-BY-SA-4.0 license](https://github.com/scicloj/clojure-data-cookbook/blob/main/LICENSE). This work is made possible by the ongoing funding I receive from [Clojurists together](https://www.clojuriststogether.org/) and my generous [Github Sponsors](https://github.com/sponsors/kiramclean).
;;
;; If you appreciate reading this book for free, there are lots of ways you can support its continued existence. Mostly you can tell people you know about Clojure and what you learned.  You can also support the book financially through [GitHub sponsors](https://github.com/sponsors/kiramclean).

;; ## Acknowledgements

;; This book wouldn't exist without the tremendous and endless support of [Daniel Slutsky](https://github.com/daslu). Many others have contributed countless hours helping me nudge it forward, notably [Raf Dittwald](https://rafd.me) and [Timothy Pratley](https://github.com/timothypratley), among many others. And of course there would be nothing to write about if it weren't for all of the amazing Clojure developers out there who contribute countless hours of free labour writing open source software. Thank you.

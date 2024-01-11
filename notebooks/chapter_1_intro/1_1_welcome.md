# Welcome

```clojure
(ns chapter-1-intro.1-1-welcome
  {:nextjournal.clerk/visibility {:code :hide}
   :nextjournal.clerk/toc true})
```

Welcome to the Clojure Data Cookbook! This is a resource for people with data to work with who want to explore what Clojure can do. Clojure is among the [most stable](https://dl.acm.org/doi/10.1145/3386321), [most loved](https://insights.stackoverflow.com/survey/2021#section-most-loved-dreaded-and-wanted-programming-scripting-and-markup-languages), and [most lucrative](https://insights.stackoverflow.com/survey/2021#section-salary-salary-and-experience-by-language) languages out there, but is not often thought of as a language for data science.

Clojure is free, open source, and has excellent performance thanks to being [hosted on the JVM](https://clojure.org/about/jvm_hosted). There are thousands of libraries available for Clojure, in addition to the endless Java libraries that are seamlessly available in Clojure, too.

Approaching a new Clojure project can be intimidating, though. It's not always obvious where to start. It's not even clear which libraries you'll need, or which ones do what. The Clojure community in general tends to favour bespoke, minimalist configurations that are unique for every project. There are good reasons for this. It tends to lead to more maintainable, more stable code, in the long run. But it does have the major downside of making it harder to get started.

Clojure and its ecosystem are very much optimized for long-term, professional use, which makes it a great fit for mission-critical projects with no room to compromise on performance or security, but this lack of frameworks and batteries-included solutions can make it feel intimidating for newcomers.

Clojure's story for data science is no different. In recent years a lot of amazing work has been done by the community that makes Clojure a great choice for many data-focused tasks, but it's not always obvious where to start.

**This book is a guide through Clojure's ecosystem for data science.** It includes some introductory tutorials on getting a Clojure environment set up, a brief overview of the most relevant parts of the Clojure language itself, and most importantly, a comprehensive collection of example-based explanations of which libraries to use and how to use them to accomplish common tasks in data science using Clojure. We'll cover data input and output, manipulation, visualisation, and finally analysis, including many common statistical analyses. The examples include a brief introduction to the type of problem we're solving, the solution itself, and in most cases an explanation of what the solution is doing. The goal is to provide insight into how to think like a Clojurian and solve problems "the Clojure way".

The examples in this book can be copy/pasted into a notebook on your own computer and should run there so you can see how they work. Continue reading this chapter for more details on how to set up a working Clojure environment on your own computer.

This book assumes some basic prior knowledge of data science but not necessarily Clojure. This is meant to be a guide to demonstrate what tools are available in Clojure's ecosystem for working with data, starting with the assumption that most people do not think of Clojure as an obvious choice for data science. It is not an introduction to statistics. The examples assume you are familiar with the underlying statistical method and offer an example implementation in Clojure.

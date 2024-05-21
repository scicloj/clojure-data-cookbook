# Why Clojure? {.unnumbered}

This is the first question most people ask me when they learn that I work primarily with Clojure. The real answer is just that I really love it. It's the first programming language I've worked with that doesn't frustrate me to my core. It never feels like I'm fighting the language to be productive. Clojure is a minimalistic, simple language that gets out the way so you can just use it as a tool to get things done. The downside is that you end up spending most of your time thinking about actual problems -- the real issues that just take time and energy to figure out. Once you have the solution, though, implementing it in Clojure is a joy.

Anyway -- as much as I personally love the language, Clojure is not a common first choice for data science (yet!). It has everything you need to be productive working with data, and (I'd argue) it offers elegant solutions to most of the most common complaints with the current data science ecosystem.

Clojure is among the [most stable](https://dl.acm.org/doi/10.1145/3386321), [most loved](https://insights.stackoverflow.com/survey/2021#section-most-loved-dreaded-and-wanted-programming-scripting-and-markup-languages), and [most lucrative](https://insights.stackoverflow.com/survey/2021#section-salary-salary-and-experience-by-language) languages out there. It's free, open source, and has excellent performance thanks to being [hosted on the JVM](https://clojure.org/about/jvm_hosted). There are thousands of libraries available for Clojure, in addition to the endless Java libraries that are seamlessly available too.

If you prefer to listen rather than read, the rest of this section is mostly a summary of the points I made in [my talk at the conj in 2023](https://www.youtube.com/watch?v=MguatDl5u2Q), which you could watch instead.

## Complete toolkit

It would be a bit of a stretch to say that Clojure is at feature parity with the current leading data science ecosystems, but it is close. Clojure's data science toolkit is complete and can support any kind of work you'd need to do with data. Some libraries are more mature than others, but every major aspect of working with data is covered. This table shows a comparison of Clojure's ecosystem to Python's and R's. Obviously there are many ways to accomplish any given task and these libraries are just a small sample of what's available, but the point is to illustrate that anything you need to do with data today, you can do in Clojure. It may not always be the most well supported or standard way to do it, but it is possible, and in many cases Clojure offers advantages over other current options, like performance and reproducibility. I'll elaborate on what I mean below.

| Tool                     | python                   | R                        | Clojure                                                 |
|--------------------------|--------------------------|--------------------------|---------------------------------------------------------|
| Math, number crunching   | SciPy (Numpy)            | base R                   | **[dtype-next](https://github.com/cnuernber/dtype-next), [Fastmath](https://github.com/generateme/fastmath), [Neanderthal](https://github.com/uncomplicate/neanderthal)** |
| Table processing         | Pandas, Polars           | data.table, dplyr, tidyr | **[tech.ml.dataset](https://github.com/techascent/tech.ml.dataset), [tablecloth](https://github.com/scicloj/tablecloth)** |
| Stats                    | StatsModels              | base R                   | **[Fastmath](https://github.com/generateme/fastmath)** |
| Dataviz                  | matplotlib               | ggplot2                  | **[cljplot](https://github.com/generateme/cljplot), [hanami](https://github.com/jsa-aerial/hanami/), ongoing work implementing ggplot in Clojure** |
| Dashboards               | Streamlit                | Shiny                    | **[DataRabbit](https://www.datarabbit.com), [Clerk](https://clerk.vision)** |
| Symbolic computation     | sympy                    | Ryacas                   | **[Emmy](https://github.com/mentat-collective/emmy)**
| Machine learning         | Scikit-learn             | caret, mlr3, tidymodels  | **[scicloj.ml](https://github.com/scicloj/scicloj.ml)** |
| Deep learning            | Keras, Pytorch           | nnet, neuralnet, h2o     | **[Deep Diamond](https://github.com/uncomplicate/deep-diamond)** |
| Scaling, parallelization | Dask                     | ---                      | **[Clojask](https://github.com/clojure-finance/clojask)** |
| Spark                    | PySpark                  | SparkR, sparklyr         | **[Geni](https://github.com/zero-one-group/geni)** |
| Developer tooling        | Jupyter Notebook, VSCode | RStudio, KnitR           | **[Clay](https://github.com/scicloj/clay), [Clerk](https://clerk.vision), [Portal](https://github.com/djblue/portal), [Calva notebooks](https://calva.io/notebooks/)** |

## Interop

The broader data science ecosystem is obviously already very developed, and there are many parts of it that will likely never be re-written. There will also always be gaps between what currently exists and Clojure's ecosystem. In these cases, it is possible to reach out to these existing ecosystems using Clojure libraries that can interface with these other languages. Currently there are libraries that enable interop with many common data science languages, listed in the table below.

| Language | Clojure interop library |
|----------|-------------------------|
| Python | [libpython-clj](https://github.com/clj-python/libpython-clj) |
| R | [ClojisR](https://github.com/scicloj/clojisr) |
| Wolfram (Mathematica) | [wolframite](https://github.com/scicloj/wolframite) |
| Julia | [libjulia-clj](https://github.com/cnuernber/libjulia-clj) |

Using these libraries, we can access existing data science tooling in other languages from Clojure. This makes the Clojure ecosystem useful for working with data even as many parts of it are still under active development. Where there are gaps, we can fill them using these bridges to other languages, which can also help reveal ideal targets for future development.

This book will cover inter-oping with other languages using these libraries in later chapters.

## Speed

Clojure is a fast language by modern programming standards, and when it needs to be, it can be as fast as Java. Typically, performance isn't an issue in Clojure and we optimize our code for readability and maintainability rather than speed. It's hard to compare languages side-by-side, but in any benchmark testing you'll find comparing modern programming languages, you can see Java is always among the top performers. Obviously Clojure is not Java, but the performance characteristics of the JVM are available in Clojure if necessary. Chris Nuernberger's work on [hamfisted](https://github.com/cnuernber/ham-fisted), [dtype-next](https://github.com/cnuernber/dtype-next), and [tech.ml.dataset](https://github.com/techascent/tech.ml.dataset) leverage a deep understanding of Java internals to implement the columnar and number processing libraries that form the foundation of Clojure's data science stack.

Leveraging the Java Virtual Machine (JVM) for execution, Clojure offers impressive performance characteristics, essential for handling large datasets and complex analyses. The ability to interoperate seamlessly with Java libraries allows Clojure to take advantage of highly optimized numerical and data processing libraries, such as those for matrix operations and parallel computing. This interoperability, combined with Clojure's efficient immutable data structures, ensures that data-intensive applications can be both fast and scalable.

## Accessible

In order for any ecosystem to gain traction, it needs to be accessible. In this context, by "accessible", I mean free and open. For better and for worse, open source software has largely won the day, which is great for the future of computing, but has the notable downside that most people expect software to be free now. It is normal and expected that professional quality tools for working with data be free, as the entire R and Python ecosystems are. Clojure and its data science stack are also free, as in beer and speech.

It's also worth elaborating a little bit on what I mean by "open", too. Open obviously refers to open source, but I also mean open for extension in a more general sense. This means the language and library ecosystem has to open and flexible enough to enable new development, but it also requires a welcoming and active community, which Clojure most definitely has. There are active communities of Clojure developers online, like the [Clojurians slack]() and the [Clojurians zulip instance](), and there are countless entry points for someone looking to learn more about Clojure or get involved in the community. If that's you, one good place to start is finding a [scicloj study group]() that reflects your interests, or introducing yourself in the [intros]() slack channel.

Clojure's accessibility stems from its open-source nature and the vibrant community surrounding it. The availability of extensive documentation, tutorials, and community support forums lowers the barrier to entry for new users. Additionally, the language's integration with the Java ecosystem opens up a vast array of libraries and tools for data processing, analysis, and visualization, further enhancing its accessibility to developers with varying backgrounds and experience levels.


## Reproducibility

Reproducibility is a priority in data science in many contexts. It's annoying to run the same program twice and get different results. It's also sometimes important for other people to be able to replicate your results. This is impossible the code you wrote produces unpredictable results in the best of times.

Reproducibility is achieved by eliminating dependencies, implicit and explicit. Although neither Python nor R enforce or require a functional style of programming, where the output of a given program depends only on its inputs (and not any hidden internal state), it's certainly still possible to write good code in any language that does not confound scopes or lead to unpredictable results. The challenge comes when the shifting state is beyond your control, like in the broader ecosystem. This is where the stability of the Clojure language itself as well as the broader ecosystem is a great asset.



Reproducibility is a big concern for data scientists
It’s annoying to run the same program twice and get different results

It’s achieved by eliminating implicit (or explicit) external dependencies.
You cannot have a reproducible process that depends on external state you do not control, like the stability of the ecosystem you depend upon.

Reproducibility is a cornerstone of scientific research and is increasingly recognized as a critical feature in data science projects. Clojure's emphasis on immutability and pure functions naturally supports the creation of reproducible data analysis pipelines. By ensuring that functions do not have side effects and that data remains unchanged unless explicitly transformed, Clojure facilitates the development of analyses that can be reliably executed to produce consistent results across different environments and over time. This predictability is crucial for validating scientific findings and sharing analyses with the broader community.


## Immutability


In Clojure, immutability is a core principle that profoundly impacts data processing and analysis workflows. Data structures in Clojure are immutable by default, meaning that once created, they cannot be changed. This design choice simplifies reasoning about data transformations and ensures that data remains consistent throughout its lifecycle. For example, when manipulating datasets, a function that transforms a dataset will produce a new version of the data without altering the original. This characteristic is invaluable for reproducibility and debugging, as it guarantees the same input will always produce the same output, devoid of side effects common in mutable environments.

## Stability

The stability of Clojure's ecosystem, rooted in its culture and language design, offers significant benefits for long-term projects. Clojure's approach to evolution, emphasizing backward compatibility and cautious introduction of changes, ensures that code written today will remain functional and relevant in the future. This stability is reflected in the slow, deliberate growth of Clojure's core language and its libraries, where changes are carefully considered to avoid disrupting existing codebases. For instance, the Clojure community values libraries that have stood the test of time and maintain compatibility across Clojure versions, making them reliable choices for critical applications.

## Functional

Clojure's functional programming paradigm is a natural fit for data science tasks, which often involve applying a series of transformations to datasets. The language's emphasis on pure functions and immutable data structures aligns well with the concept of data pipelines, where each step cleanly maps inputs to outputs without side effects. An example of this is the use of Clojure's sequence processing functions (such as map, filter, and reduce) to perform complex data manipulations in a concise and readable manner. These functions abstract the complexity of data processing, allowing developers to focus on the logic of their transformations rather than the mechanics of iteration and state management.

## Better "notebooks"

Clojure provides a superior alternative to traditional notebook environments like Jupyter through its interactive development tools and REPL-driven programming. Tools such as Calva, a Clojure development plugin for Visual Studio Code, offer integrated support for notebook-style coding, enabling data scientists to execute Clojure code in an interactive session and visualize results inline. This combines the exploratory flexibility of notebooks with the robustness of a compiled language, enhancing code reusability and reducing the likelihood of errors associated with out-of-order execution, which plagues traditional notebooks.

## Portability

The portability of Clojure code, especially in the context of data science, streamlines workflows from analysis to publication. Clojure's capability to integrate code, visualization, and documentation within a single environment (such as a namespace) eliminates the friction of moving data and results between different tools. An illustrative example is the ability to perform data analysis, generate visualizations, and compile results into a report or presentation, all within the same Clojure project. This seamless workflow reduces overhead and enhances productivity by keeping the entire process in a unified, portable format.

## Ergonomics

Clojure's syntax and data-centric design principles provide an ergonomic environment for data manipulation and analysis. The language's emphasis on simplicity and its powerful abstraction mechanisms make complex operations on data more intuitive and less error-prone. For instance, Clojure's rich set of data structure manipulation functions, such as those for handling vectors, maps, and sets, allow for expressive and concise data queries and transformations. This ergonomic design reduces the cognitive load on developers, enabling them to focus on the essence of their data processing tasks.

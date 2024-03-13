# Why Clojure? {.unnumbered}

This is the first question most people ask me when they learn that I work primarily with Clojure. The real answer is just that I really love it. It's the first programming language I've worked with that doesn't frustrate me to my core. It never feels like I'm fighting the language to be productive. Clojure is a minimalistic, simple language that gets out the way so you can just use it as a tool to get things done. The downside is that you end up spending most of your time thinking about actual problems -- the real issues that just take time and energy to figure out. Once you have the solution, though, implementing it in Clojure is a joy.

Anyway -- as much as I personally love the language, Clojure is not a common first choice for data science (yet!). It has everything you need to be productive working with data, and (I'd argue) it offers elegant solutions to most of the most common complaints with the current data science ecosystem.

Clojure is among the [most stable](https://dl.acm.org/doi/10.1145/3386321), [most loved](https://insights.stackoverflow.com/survey/2021#section-most-loved-dreaded-and-wanted-programming-scripting-and-markup-languages), and [most lucrative](https://insights.stackoverflow.com/survey/2021#section-salary-salary-and-experience-by-language) languages out there. It's free, open source, and has excellent performance thanks to being [hosted on the JVM](https://clojure.org/about/jvm_hosted). There are thousands of libraries available for Clojure, in addition to the endless Java libraries that are seamlessly available too.

If you prefer to listen rather than read, the rest of this section is mostly a summary of the points I made in [my talk at the 2023 Conj](https://www.youtube.com/watch?v=MguatDl5u2Q), which you could watch instead.

## Complete toolkit

It would be a bit of a stretch to say that Clojure is at feature parity with the current leading data science ecosystems, but it is close. Clojure's data science toolkit is complete and can support any kind of work you'd need to do with data. Some libraries are more mature than others, but every major aspect of working with data is covered. This table shows a comparison of Clojure's ecosystem to Python's and R's. Obviously there are many ways to accomplish any given task and these libraries are just a small sample of what's available, but the point is to illustrate that anything you need to do with data today, you (technically) can do in Clojure. It will not be the most well supported or standard way to do it, but it is possible, and in many cases Clojure offers improvements over other current options.

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

Clojure is a fast language by modern programming standards, and when it needs to be, it can be as fast as Java. Typically, performance isn't an issue in Clojure and we optimize our code for readability and maintainability rather than speed. It's hard to compare languages side-by-side, but in any benchmark testing you'll find comparing modern programming languages, you can see Java is always among the top performers. Obviously Clojure is not Java, but the performance characteristics of the JVM are available in Clojure if necessary. Chris Nuremburger's work on [hamfisted](), [dtype-next](), and [tech.ml.dataset]() leverage a deep understanding of Java internals to implement the columnar and number processing libraries that form the foundation of Clojure's data science stack.

## Accessible

In order for any ecosystem to gain traction, it needs to be accessible. In this context, by "accessible", I mean free and open. For better and for worse, open source software has largely won the day, which is great for the future of computing, but has the notable downside that most people expect software to be free now. It is normal and expected that professional quality tools for working with data be free, as the entire R and Python ecosystems are. Clojure and its data science stack are also free, as in beer and speech.

It's also worth elaborating a little bit on what I mean by "open", too. Open obviously refers to open source, but I also mean open for extension in a more general sense. This means the language and library ecosystem has to open and flexible enough to enable new development, but it also requires a welcoming and active community, which Clojure most definitely has. There are active communities of Clojure developers online, like the [Clojurians slack]() and the [Clojurians zulip instance](), and there are countless entry points for someone looking to learn more about Clojure or get involved in the community. If that's you, one good place to start is finding a [scicloj study group]() that reflects your interests, or introducing yourself in the [intros]() slack channel.

## Reproducibility

Reproducibility is a priority in data science in many contexts. It's annoying to run the same program twice and get different results. It's also sometimes important for other people to be able to replicate your results. This is impossible the code you wrote produces unpredictable results in the best of times.

Reproducibility is achieved by eliminating dependencies, implicit and explicit. Although neither Python nor R enforce or require a functional style of programming, where the output of a given program depends only on its inputs (and not any hidden internal state), it's certainly still possible to write good code in any language that does not confound scopes or lead to unpredictable results. The challenge comes when the shifting state is beyond your control, like in the broader ecosystem. This is where the stability of the Clojure language itself as well as the broader ecosystem is a great asset.



Reproducibility is a big concern for data scientists
It’s annoying to run the same program twice and get different results

It’s achieved by eliminating implicit (or explicit) external dependencies.
You cannot have a reproducible process that depends on external state you do not control, like the stability of the ecosystem you depend upon.


## Immutability

## Stability

## Functional

## Better "notebooks"

## Portability

## Ergonomics

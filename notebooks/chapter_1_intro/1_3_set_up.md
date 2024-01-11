```clojure
(ns chapter-1-intro.1-3-set-up
  {:nextjournal.clerk/visibility {:code :hide}
   :nextjournal.clerk/toc true})
```

# Setting up a Clojure environment

This is a guide for setting up a Clojure environment if you're new to the language. If you already have a Clojure environment running on your machine, you can skip this part!

## Install Clojure and its dependencies

The [official Clojure website](https://clojure.org/guides/install_clojure) maintains the latest instructions for installing Clojure and its dependencies on your system. The most up-to-date instructions for your operating system will be found there.

## Install an editor/IDE for working with Clojure

Clojure works well with a variety of editors and Integrated Development Environments (IDEs). Some popular choices include:

- **Visual Studio Code + Calva:** The most beginner-friendly and lightweight option for a fully-functional, customizable Clojure development environment. If you don't have an editor or environment set up already that you prefer, Visual Studio Code and Calva offer the lowest-friction path to getting started with Clojure.
- **Emacs + CIDER:** Emacs + [CIDER](https://cider.mx) is the most common IDE setup amongst Clojure developers, but it also has the steepest learning curve. One does not just _use_ Emacs. Learning how to be effective with it is a journey, but it's worth it. If you're a vim-user but don't want to feel left out in the community, you might be interested in [Spacemacs](https://develop.spacemacs.org).
- IntelliJ IDEA + Cursive: IntelliJ is one of the most common editors amongst Java developers, and with the [Cursive IDE](https://cursive-ide.com) it makes for an excellent Clojure development experience. You might prefer this if you like a batteries-included IDE experience but hate VSCode and/or like paying for things. (I'm being slightly facetious -- there is a free, somewhat feature-depleted, version of IntelliJ available (called the "community edition") and Cursive is free for non-commercial use.)

Choose an editor or IDE that suits your preferences and install the necessary plugins or extensions for Clojure development.k

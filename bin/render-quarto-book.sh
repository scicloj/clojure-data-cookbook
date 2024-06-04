set -euo pipefail


# Render cookbook as a quarto book
#######

function echoOrange() {
    ORANGE='\033[38;5;208m'
    RESET='\033[0m'
    printf "${ORANGE}$1${RESET}"
}

# cd into the directory that this script is in
cd "$(dirname "$0")"

cd ..
echoOrange ">>>>>> Building quarto book with clay...\n"
clojure -A:dev -X dev/build-cli

cd docs
echoOrange ">>>>>> Updating _quarto.yml...\n"
cp ../env/dev/quarto.yml ../docs/_quarto.yml

# echoOrange ">>>>>> Publishing to gh-pages with quarto publish\n"
quarto preview
# quarto publish gh-pages --no-prompt

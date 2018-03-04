#!/bin/bash
## Description: Verifies if all dependencies are installed.
## Author: Henrique Rosa (henrique.rosa@telestax.com)

verifyJava() {
    if [[ -z "$(which java)" ]] || [[ $(java -version) != "1.8.*" ]]; then
        echo "Java 8 dependency is missing."
        echo "CentOS/RHEL: yum install java-1.8.0-openjdk-devel"
        echo "Debian/Ubuntu:"
        echo "    add-apt-repository ppa:openjdk-r/ppa"
        echo "    apt-get update"
        echo "    apt-get install openjdk-8-jdk"
        echo "macOS: "
        echo "    brew tap caskroom/versions"
        echo "    brew cask install java8"
        exit 1
    fi
}

verifyTmux() {
    if [ -z "$(which tmux)" ]; then
        echo "tmux dependency is missing."
        echo "CentOS/RHEL: yum install tmux"
        echo "Debian/Ubuntu: apt-get install tmux"
        echo "macOS: brew install tmux"
        exit 1
    fi
}

verifyJava
verifyTmux

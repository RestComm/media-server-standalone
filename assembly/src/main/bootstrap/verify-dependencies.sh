#!/bin/bash
## Description: Verifies if all dependencies are installed.
## Author: Henrique Rosa (henrique.rosa@telestax.com)

verifyJava() {

    if [[ -z "$(which java)" ]]; then
        printJavaInstallation
        exit 1
    else
        local java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f2)
        if [[ $(java -version) -lt "8" ]]; then
            printJavaInstallation
            exit 1
        fi
    fi
}

printJavaInstallation() {
    echo "Java 8 dependency is missing."
    echo "CentOS/RHEL: yum install java-1.8.0-openjdk-devel"
    echo "Debian/Ubuntu:"
    echo "    add-apt-repository ppa:openjdk-r/ppa"
    echo "    apt-get update"
    echo "    apt-get install openjdk-8-jdk"
    echo "macOS: "
    echo "    brew tap caskroom/versions"
    echo "    brew cask install java8"
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

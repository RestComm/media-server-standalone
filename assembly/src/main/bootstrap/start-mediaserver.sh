#! /bin/bash
## Description: Starts Media Server with auto-configuration.
## Author     : Henrique Rosa (henrique.rosa@telestax.com)
## Parameters : 1. path to conf file (optional)

DIRNAME=$(dirname "${BASH_SOURCE[0]}")

verifyDependencies() {
    source $DIRNAME/.verify-dependencies.sh
}

configureMediaServer() {
    # Set permissions of run script because it may have been overwritten by commands like sed
    chmod 755 $DIRNAME/run.sh
}

startMediaServer() {
    echo 'Starting RestComm Media Server...'
    if tmux ls | grep -q 'mediaserver'; then
        echo '... already running a session named "mediaserver"! Aborted.'
        exit 1
    else
        tmux new -s mediaserver -d $DIRNAME/run.sh
        echo '...RestComm Media Server started running on session named "mediaserver"!'
    fi
}

verifyDependencies
configureMediaServer
startMediaServer
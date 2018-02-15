# RestComm Media Server, Community Edition

**RestComm Media Server, Community Edition** (RMS) is a standalone real-time Media Server developed in Java.

It offers a set of rich media features, such as streaming, conferencing, recording, playback, IVR, text-to-speech, etc.
It also supports and is compliant with [WebRTC](http://www.webrtc.org/).

The Media Server can be accessed programmatically, using either MGCP or JSR 309 drivers.
Both drivers run in Java EE, SIP Servlets or JSLEE containers.

Thanks to its pluggable architecture, the Media Server binary is basically an assembly of the [media-core framework](https://github.com/RestComm/media-core) plus a set of plugins that define the behaviour of specific features, such as ASR, VAD, Codecs, etc.
Users can enable or disable plugins in the Media Server configuration, thus easily adapting the behaviour of the Media Server to their requirements.  

![RestComm Media Projects](http://www.plantuml.com/plantuml/png/TL1B2iCW4Drx2ib-2j5j5hdADDeY-0bgUlvEJB6YPEFuvdtWpPeondmuosYO5swPJquhWx25g2UEYP-VWNq6LqYQ0vTq_EW8bEmLsk2lc3yS3BiztiR3N7GNcEz4eX69Ev5iH9Anim5V06fsjn3zedeUfU3FHh35eiEoOaPFIMiwhzehElCN)

RestComm Media Server is led by [TeleStax, Inc.](http://www.telestax.com) and developed in collaboration with a community of individual and enterprise contributors.

## Issue Tracker

Please refer to the [RestComm Media - Community Issue Tracker](https://telestax.atlassian.net/projects/RMS/issues) to report issues about any media-related community project.

When reporting issues, please identify the affected [media component](https://telestax.atlassian.net/projects/RMS?selectedItem=com.atlassian.jira.jira-projects-plugin:components-page).

## Community [![https://gitter.im/RestComm/mediaserver](https://badges.gitter.im/RestComm/mediaserver.svg)](https://gitter.im/RestComm/mediaserver?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

You can become part of the Open Source RestComm project family by contributing with patches, documentation or tests. Read our [Contributors Guide](https://github.com/RestComm/restcomm/wiki/Contribute-to-RestComm) and [Open Source Playbook](https://telestax.com/wp-content/uploads/2016/04/TeleStaxOpenSourcePlaybook.pdf) to get started and check the [Media Server Roadmap](https://github.com/RestComm/mediaserver/milestones) for open issues marked as [Help Wanted](https://github.com/RestComm/mediaserver/issues?q=is%3Aissue+is%3Aopen+label%3Ahelp-wanted).

Help us improve the project by [asking questions](https://groups.google.com/forum/#!forum/restcomm), [reporting bugs](https://telestax.atlassian.net/projects/RMS/issues) and [contributing back](https://github.com/RestComm/media-server-standalone/pulls).
**Your Feedback is highly appreciated!**

Top contributors will be given the opportunity to [apply for a job](https://telestax.com/jobs/) at TeleStax!

Join our vibrant community and [download the latest stable build NOW](https://www.restcomm.com/downloads/)!

### Acknowledgements

We [publicly acknowledge our contributors]((http://www.telestax.com/opensource/acknowledgments/)). A big **THANK YOU** to all of you, who help us drive the project forward and ensure its quality!

Many thanks to [SIPME](https://www.sipme.me/) for the ongoing support and contributions since 2010.

## License [![FOSSA Status](https://app.fossa.io/api/projects/git%2Bhttps%3A%2F%2Fgithub.com%2FRestComm%2Fmediaserver.svg?type=shield)](https://app.fossa.io/projects/git%2Bhttps%3A%2F%2Fgithub.com%2FRestComm%2Fmediaserver?ref=badge_shield)

The project is licensed under dual license policy, depending on whether users adopt the Community Edition or the Commercial Edition. 

The **Community Edition** project is licensed under the Free Open Source GNU Affero GPL v3.0.

Alternatively, the **Commercial Edition** requires a commercial license which can be obtained by [contacting TeleStax](http://www.telestax.com/contactus/#InquiryForm).

[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bhttps%3A%2F%2Fgithub.com%2FRestComm%2Fmediaserver.svg?type=large)](https://app.fossa.io/projects/git%2Bhttps%3A%2F%2Fgithub.com%2FRestComm%2Fmediaserver?ref=badge_large)
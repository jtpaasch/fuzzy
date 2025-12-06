lazy val lib = (project in file("lib"))
lazy val app = (project in file("app")).dependsOn(lib)
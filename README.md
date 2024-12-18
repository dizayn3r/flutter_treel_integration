# flutter_treel_integration

## Add maven block in build.gradle
Replace <AUTH_TOKEN> with token provided
`` gradle
    maven {
            url "https://jitpack.io"
            credentials { username "<AUTH_TOKEN>" }
            }
``


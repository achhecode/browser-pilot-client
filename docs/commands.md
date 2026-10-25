Building MacOS App

```sh

mvn clean javafx:jlink

find target -maxdepth 3 -type f | head -50

ls target/image/bin/java
ls target/classes/module-info.class
target/image/bin/java --version


jpackage \
  --type app-image \
  --name "Linkedin Puzzle Solver" \
  --app-version 1.0.0 \
  --vendor "Achhe Code" \
  --module com.achhecode.browserpilot.client/com.achhecode.browserpilot.client.App \
  --runtime-image target/image \
  --dest target/dist


open "target/dist/Linkedin Puzzle Solver.app"

```

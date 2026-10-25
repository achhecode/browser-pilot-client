# Creating and Publishing a macOS Release

## 1. Prerequisites

- macOS with a compatible JDK installed.
- Maven installed.
- Git and GitHub access.
- The project builds successfully with `mvn clean javafx:jlink`.

## 2. Build the Application

From the project root, run:

```bash
mvn clean javafx:jlink
```

This creates a custom Java runtime image in `target/image`.

## 3. Package the macOS App

Create the application bundle:

```bash
jpackage \
  --type app-image \
  --name "Linkedin Puzzle Solver" \
  --app-version 1.0.0 \
  --vendor "Achhe Code" \
  --module com.achhecode.browserpilot.client/com.achhecode.browserpilot.client.App \
  --runtime-image target/image \
  --dest target/dist
```

The application will be created at:

```text
target/dist/Linkedin Puzzle Solver.app
```

Test it before publishing:

```bash
open "target/dist/Linkedin Puzzle Solver.app"
```

Confirm that the UI launches and that the client can connect to the required backend services.

## 4. Create a ZIP for GitHub

Compress the `.app` bundle so users can download it:

```bash
cd target/dist
ditto -c -k --sequesterRsrc --keepParent \
  "Linkedin Puzzle Solver.app" \
  "Linkedin-Puzzle-Solver-macos-arm64-1.0.0.zip"
```

The `arm64` label indicates an Apple Silicon build. Use a different architecture label if you build for Intel Macs.

## 5. Publish a GitHub Release

1. Push the latest source code to GitHub.
2. Open the repository on GitHub.
3. Select **Releases → Draft a new release**.
4. Create a new tag, such as `v1.0.0`.
5. Choose the target branch containing the release source.
6. Enter a release title, such as `Linkedin Puzzle Solver v1.0.0`.
7. Add the release notes.
8. Upload `Linkedin-Puzzle-Solver-macos-arm64-1.0.0.zip` under **Attach binaries**.
9. Select **Publish release**.

## 6. Suggested Release Notes

**Linkedin Puzzle Solver v1.0.0**

### Features
- JavaFX desktop interface for supported LinkedIn puzzle games.
- Separate Pinpoint answer and game-solving workflows.
- Configurable backend service ports.
- JSON request and response editing.

### Installation
1. Download the macOS ZIP asset.
2. Extract it and move `Linkedin Puzzle Solver.app` to the Applications folder.
3. Open the application.

### Requirements
- Compatible macOS version and processor architecture.
- Pinpoint Service and LinkedIn Games Service running at the configured ports.
- A configured Chrome session if browser automation is required.

### Notes
This release bundles a Java runtime, so a separate Java installation is not required to run the desktop client. The backend services and browser session are distributed and managed separately.

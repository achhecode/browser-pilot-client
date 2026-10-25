# Browser Pilot Client

## About

Browser Pilot Client is a JavaFX desktop application used to interact with the backend services of the [**Browser Pilot**](https://github.com/achhecode/browser-pilot) project. It provides a graphical interface for sending requests to backend APIs, viewing responses, and executing supported puzzle-solving workflows.

## Features

- JavaFX-based desktop interface.
- Integration with Pinpoint Service and LinkedIn Games Service.
- Configurable backend service ports.
- JSON request and response handling.
- Support for ZIP, WEND, TANGO, QUEENS, PATCHES, and MINI SUDOKU games.
- Packaged macOS application with a bundled Java runtime.

## Requirements

To run the packaged macOS application:

- macOS compatible with the release build.
- Browser Pilot backend services running and accessible.
- Chrome running with the required remote debugging configuration, if browser automation is needed.

**Java and Maven are not required to run the packaged `.app`.**

## Getting Started

### 1. Download

1. Open the repository's **Releases** page on GitHub.
2. Download the latest macOS ZIP asset.
3. Extract the ZIP file.

### 2. Launch

Open `Linkedin Puzzle Solver.app`.

If macOS blocks the application, review the security warning under **System Settings → Privacy & Security** and allow the application only if you trust its source.

### 3. Configure Backend Services

Start the required backend services and configure their ports in the application.

Default ports:

| Service | Port |
|---|---:|
| Pinpoint Service | `8081` |
| LinkedIn Games Service | `8082` |

Ensure the services are reachable before submitting requests.

## Usage

1. Select the puzzle game.
2. Enter the required Pinpoint request JSON.
3. Click **Get Answer** to call the Pinpoint Service.
4. Review the response.
5. Click **Paste below** to prepare the game-solving request.
6. Review or modify the request JSON.
7. Click **Solve Now** to call the LinkedIn Games Service.

## Development

The project uses Java, JavaFX, Maven, and Java's built-in HTTP client.

To build the project from source, install a compatible JDK and Maven, then run:

```bash
mvn clean javafx:jlink
```

To launch the application during development:

```bash
mvn javafx:run
```

To package the application for macOS, use `jpackage` with the generated runtime image under `target/image`.

## Related Services

This client depends on the backend services provided by the Browser Pilot project:

- **Pinpoint Service** — calculates puzzle solutions.
- **LinkedIn Games Service** — receives solutions and interacts with the connected browser.

These services are managed and deployed separately from the desktop client.

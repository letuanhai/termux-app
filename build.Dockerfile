# Builds the app in a container, so no jdk, android sdk or ndk is needed on the host:
#
#   podman build -t termux-build -f build.Dockerfile .
#   podman run --rm -v "$PWD":/src -w /src -v termux-gradle:/root/.gradle termux-build ./gradlew assembleDebug
#
# Use docker instead of podman if preferred, and add :Z to the /src mount if SELinux is
# enforcing. The termux-gradle volume keeps the gradle caches out of the repository between
# builds. The debug APKs are built to app/build/outputs/apk/debug and are signed with the
# untrusted test key in the repository, so an already installed Termux app must be
# uninstalled first.
#
# Replace assembleDebug with test to run the unit tests, or with assembleRelease to build
# unsigned release APKs. Pass -e TERMUX_PACKAGE_VARIANT=apt-android-5 to podman run to build
# the apt-android-5 bootstrap variant instead of the default apt-android-7.

FROM docker.io/library/eclipse-temurin:17-jdk

ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

RUN apt-get update && apt-get install -y --no-install-recommends unzip curl make \
 && rm -rf /var/lib/apt/lists/* \
 && mkdir -p $ANDROID_HOME/cmdline-tools \
 && curl -sL https://dl.google.com/android/repository/commandlinetools-linux-13114758_latest.zip -o /tmp/cmdline-tools.zip \
 && unzip -q /tmp/cmdline-tools.zip -d $ANDROID_HOME/cmdline-tools \
 && mv $ANDROID_HOME/cmdline-tools/cmdline-tools $ANDROID_HOME/cmdline-tools/latest \
 && rm /tmp/cmdline-tools.zip \
 && yes | sdkmanager --licenses > /dev/null \
 && sdkmanager "platforms;android-36" "build-tools;36.0.0" "ndk;29.0.14206865" "platform-tools"

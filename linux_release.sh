#!/bin/bash
set -e

echo "--- [1/3] Building .deb package via Gradle..."
./gradlew :app-desktop:packageDeb

# Find the latest .deb package
DEB_FILE=$(find app-desktop/build/compose/binaries/main/deb -name "*.deb" | head -n 1)
[ -z "$DEB_FILE" ] && { echo "ERROR: .deb package not found!"; exit 1; }

echo "--- [2/3] Patching package: $DEB_FILE"
TEMP_DIR="build/temp_deb_patch"
rm -rf "$TEMP_DIR" && mkdir -p "$TEMP_DIR"

# Extract all package contents
dpkg-deb -R "$DEB_FILE" "$TEMP_DIR"

# Search for the main .desktop file in /opt/ (which is linked to /usr/share/applications)
DESKTOP_FILE=$(find "$TEMP_DIR/opt" -type f -name "*.desktop" | head -n 1)

if [ -n "$DESKTOP_FILE" ]; then
    echo "Found shortcut: $DESKTOP_FILE"

    # Define Display Name
    APP_NAME="CommandsBox"

    # PATCHING THE FILE:
    # 1. Remove old Name and StartupWMClass lines if they exist
    sed -i '/^Name=/d' "$DESKTOP_FILE"
    sed -i '/^StartupWMClass=/d' "$DESKTOP_FILE"

    # 2. Insert the new Name right after the [Desktop Entry] header
    sed -i "/\[Desktop Entry\]/a Name=$APP_NAME" "$DESKTOP_FILE"

    # 3. Append StartupWMClass to the end of the file
    # Note: Ensure this string matches your actual MainKt class path in the desktop module
    echo "StartupWMClass=com-magictools-commandsbox-appdesktop-MainKt" >> "$DESKTOP_FILE"

    echo "--- [OK] Desktop file patched (Name=$APP_NAME, StartupWMClass=...)"

    echo "--- [3/3] Repacking the .deb file..."
    dpkg-deb -b "$TEMP_DIR" "$DEB_FILE"
    echo "--- DONE! Package is fixed."
else
    echo "ERROR: .desktop file not found inside the package!"
    rm -rf "$TEMP_DIR"
    exit 1
fi

rm -rf "$TEMP_DIR"
echo "------------------------------------------------"
echo "Package ready: $DEB_FILE"
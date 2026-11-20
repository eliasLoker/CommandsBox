import XCTest

final class CommandsBoxNativeUITests: XCTestCase {

    private var app: XCUIApplication!

    override func setUpWithError() throws {
        continueAfterFailure = false
        app = XCUIApplication()
        app.launchArguments += [
            "-AppleLanguages", "(en)",
            "-AppleLocale", "en_US"
        ]
        app.launch()
    }

    func testHomeFlowsAndSettings() throws {
        XCTAssertTrue(
            app.staticTexts["Add first tab"].waitForExistence(timeout: 15),
            "Home should appear after the splash"
        )

        app.buttons["add_banner_expand_button"].tap()
        type("UITestTab", into: field("add_tab_input"))
        app.buttons["add_tab_button"].tap()
        XCTAssertTrue(app.staticTexts["UITestTab"].waitForExistence(timeout: 5))

        XCTAssertTrue(field("add_command_input").waitForExistence(timeout: 5))
        type("echo hello", into: field("add_command_input"))
        type("Test description", into: field("add_description_input"))
        app.buttons["add_command_button"].tap()
        XCTAssertTrue(app.staticTexts["echo hello"].waitForExistence(timeout: 5))
        XCTAssertTrue(app.staticTexts["Test description"].exists)

        button(prefix: "command_copy_").tap()
        XCTAssertTrue(app.staticTexts["Command copied!"].waitForExistence(timeout: 5))
        XCTAssertTrue(
            waitForDisappearance(app.staticTexts["Command copied!"], timeout: 8),
            "The toast should be dismissed by the shared timer"
        )

        button(prefix: "command_edit_").tap()
        let editCommand = field(prefix: "command_edit_command_")
        XCTAssertTrue(editCommand.waitForExistence(timeout: 5))
        clearButton(prefix: "command_edit_command_").tap()
        editCommand.tap()
        editCommand.typeText("echo edited")
        app.staticTexts["CommandsBox"].tap()
        button(prefix: "command_apply_edit_").tap()
        XCTAssertTrue(app.staticTexts["echo edited"].waitForExistence(timeout: 5))

        type("second command", into: field("add_command_input"))
        type("Second description", into: field("add_description_input"))
        app.buttons["add_command_button"].tap()
        XCTAssertTrue(app.staticTexts["second command"].waitForExistence(timeout: 5))

        let upButtons = app.buttons.matching(
            NSPredicate(format: "identifier BEGINSWITH 'command_up_'")
        )
        XCTAssertGreaterThan(upButtons.count, 1)
        upButtons.element(boundBy: upButtons.count - 1).tap()

        let second = app.staticTexts["second command"]
        let edited = app.staticTexts["echo edited"]
        XCTAssertTrue(second.waitForExistence(timeout: 5))
        XCTAssertLessThan(second.frame.minY, edited.frame.minY, "The second command moved up")

        button(prefix: "command_delete_").tap()
        XCTAssertTrue(app.staticTexts["Delete command"].waitForExistence(timeout: 5))
        app.buttons["dialog_confirm_button"].tap()
        XCTAssertTrue(waitForDisappearance(second, timeout: 5))

        button(prefix: "tab_edit_").tap()
        XCTAssertTrue(app.staticTexts["Edit tab"].waitForExistence(timeout: 5))
        let tabName = field("dialog_tab_name_input")
        clear(tabName)
        tabName.typeText("UITestTabRenamed")
        app.staticTexts["Edit tab"].tap()
        app.buttons["dialog_confirm_button"].tap()
        XCTAssertTrue(app.staticTexts["UITestTabRenamed"].waitForExistence(timeout: 5))

        app.buttons["home_settings_button"].tap()
        XCTAssertTrue(app.staticTexts["Settings"].waitForExistence(timeout: 5))
        XCTAssertTrue(app.staticTexts["Current theme: Light"].exists)

        app.buttons["settings_theme_switch"].tap()
        XCTAssertTrue(app.staticTexts["Current theme: Dark"].waitForExistence(timeout: 3))
        app.buttons["settings_theme_switch"].tap()
        XCTAssertTrue(app.staticTexts["Current theme: Light"].waitForExistence(timeout: 3))

        app.buttons["settings_back_button"].tap()
        XCTAssertTrue(app.staticTexts["UITestTabRenamed"].waitForExistence(timeout: 5))

        button(prefix: "tab_delete_").tap()
        XCTAssertTrue(app.staticTexts["Delete tab"].waitForExistence(timeout: 5))
        app.buttons["dialog_confirm_button"].tap()
        XCTAssertTrue(app.staticTexts["Add first tab"].waitForExistence(timeout: 5))
    }

    func testDescriptionInputShowsTypedText() throws {
        XCTAssertTrue(app.staticTexts["Add first tab"].waitForExistence(timeout: 15))

        app.buttons["add_banner_expand_button"].tap()
        type("Descriptions", into: field("add_tab_input"))
        app.buttons["add_tab_button"].tap()
        XCTAssertTrue(app.staticTexts["Descriptions"].waitForExistence(timeout: 5))

        type("echo described", into: field("add_command_input"))

        let descriptionText = "Visible description that wraps onto a second line"
        let description = field("add_description_input")
        XCTAssertTrue(description.waitForExistence(timeout: 5))
        description.tap()
        description.typeText(descriptionText)
        XCTAssertGreaterThan(
            description.frame.height,
            0,
            "The focused description input must render the typed text"
        )
        XCTAssertEqual(
            description.value as? String,
            descriptionText,
            "The focused description input must keep the typed text"
        )

        app.buttons["add_command_button"].tap()
        XCTAssertTrue(app.staticTexts["echo described"].waitForExistence(timeout: 5))
        XCTAssertTrue(app.staticTexts[descriptionText].waitForExistence(timeout: 5))

        button(prefix: "command_edit_").tap()
        let editDescription = field(prefix: "command_edit_description_")
        XCTAssertTrue(editDescription.waitForExistence(timeout: 5))
        clearButton(prefix: "command_edit_description_").tap()
        editDescription.tap()
        editDescription.typeText("Edited description")
        XCTAssertGreaterThan(
            editDescription.frame.height,
            0,
            "The focused edit description input must render the typed text"
        )
        XCTAssertEqual(
            editDescription.value as? String,
            "Edited description",
            "The focused edit description input must keep the typed text"
        )

        app.staticTexts["CommandsBox"].tap()
        button(prefix: "command_apply_edit_").tap()
        XCTAssertTrue(app.staticTexts["Edited description"].waitForExistence(timeout: 5))

        button(prefix: "command_delete_").tap()
        XCTAssertTrue(app.staticTexts["Delete command"].waitForExistence(timeout: 5))
        app.buttons["dialog_confirm_button"].tap()
        XCTAssertTrue(waitForDisappearance(app.staticTexts["echo described"], timeout: 5))

        button(prefix: "tab_delete_").tap()
        XCTAssertTrue(app.staticTexts["Delete tab"].waitForExistence(timeout: 5))
        app.buttons["dialog_confirm_button"].tap()
        XCTAssertTrue(app.staticTexts["Add first tab"].waitForExistence(timeout: 5))
    }

    func testTabSelectionAndReorder() throws {
        XCTAssertTrue(app.staticTexts["Add first tab"].waitForExistence(timeout: 15))

        app.buttons["add_banner_expand_button"].tap()
        type("TabA", into: field("add_tab_input"))
        app.buttons["add_tab_button"].tap()
        XCTAssertTrue(app.staticTexts["TabA"].waitForExistence(timeout: 5))

        type("TabB", into: field("add_tab_input"))
        app.buttons["add_tab_button"].tap()
        XCTAssertTrue(app.staticTexts["TabB"].waitForExistence(timeout: 5))

        app.staticTexts["TabB"].tap()
        let left = button(prefix: "tab_left_")
        XCTAssertTrue(left.waitForExistence(timeout: 5))
        left.tap()

        let tabA = app.staticTexts["TabA"]
        let tabB = app.staticTexts["TabB"]
        XCTAssertTrue(tabA.waitForExistence(timeout: 5))
        XCTAssertLessThan(tabB.frame.minX, tabA.frame.minX, "TabB moved left")

        button(prefix: "tab_right_").tap()
        XCTAssertGreaterThan(tabB.frame.minX, tabA.frame.minX, "TabB moved right")

        button(prefix: "tab_delete_").tap()
        app.buttons["dialog_confirm_button"].tap()
        XCTAssertTrue(waitForDisappearance(tabB, timeout: 5))

        XCTAssertTrue(tabA.waitForExistence(timeout: 5))
        tabA.tap()
        button(prefix: "tab_delete_").tap()
        app.buttons["dialog_confirm_button"].tap()
        XCTAssertTrue(app.staticTexts["Add first tab"].waitForExistence(timeout: 5))
    }

    func testGermanLocalization() throws {
        app.terminate()

        let german = XCUIApplication()
        german.launchArguments = [
            "-AppleLanguages", "(de)",
            "-AppleLocale", "de_DE"
        ]
        german.launch()

        XCTAssertTrue(
            german.staticTexts["Ersten Tab hinzufügen"].waitForExistence(timeout: 15),
            "The German Moko resource should be used"
        )
        XCTAssertTrue(german.staticTexts["Befehlsliste ist leer"].exists)
        german.buttons["add_banner_expand_button"].tap()
        XCTAssertTrue(
            german.staticTexts["Tab-Name"].waitForExistence(timeout: 5),
            "The German ViewModel resource should be used"
        )
        german.terminate()
    }

    func testZZScreenshots() throws {
        XCTAssertTrue(app.staticTexts["Add first tab"].waitForExistence(timeout: 15))

        app.buttons["add_banner_expand_button"].tap()
        type("Docker", into: field("add_tab_input"))
        app.buttons["add_tab_button"].tap()
        XCTAssertTrue(app.staticTexts["Docker"].waitForExistence(timeout: 5))

        type("docker ps --format \"table {{.Names}}\"", into: field("add_command_input"))
        type(
            "List Containers: Shows only the names and status of currently running containers in a clean table.",
            into: field("add_description_input")
        )
        app.buttons["add_command_button"].tap()
        XCTAssertTrue(app.staticTexts["docker ps --format \"table {{.Names}}\""].waitForExistence(timeout: 5))

        type("docker system prune -a --volumes", into: field("add_command_input"))
        type(
            "Deep Clean: Removes all unused containers, networks, images, and volumes to reclaim disk space.",
            into: field("add_description_input")
        )
        app.buttons["add_command_button"].tap()
        XCTAssertTrue(app.staticTexts["docker system prune -a --volumes"].waitForExistence(timeout: 5))

        app.buttons["add_banner_arrow_button"].tap()
        XCTAssertTrue(waitForDisappearance(field("add_tab_input"), timeout: 3))
        attachScreenshot("home-light-populated")

        button(prefix: "command_delete_").tap()
        XCTAssertTrue(app.staticTexts["Delete command"].waitForExistence(timeout: 5))
        attachScreenshot("dialog-light")
        app.buttons["dialog_dismiss_button"].tap()

        app.buttons["home_settings_button"].tap()
        XCTAssertTrue(app.staticTexts["Settings"].waitForExistence(timeout: 5))
        attachScreenshot("settings-light")

        app.buttons["settings_theme_switch"].tap()
        XCTAssertTrue(app.staticTexts["Current theme: Dark"].waitForExistence(timeout: 3))
        attachScreenshot("settings-dark")

        app.buttons["settings_back_button"].tap()
        XCTAssertTrue(app.staticTexts["Docker"].waitForExistence(timeout: 5))
        attachScreenshot("home-dark-populated")

        app.buttons["home_settings_button"].tap()
        app.buttons["settings_theme_switch"].tap()
        XCTAssertTrue(app.staticTexts["Current theme: Light"].waitForExistence(timeout: 3))
        app.buttons["settings_back_button"].tap()
        XCTAssertTrue(app.staticTexts["Docker"].waitForExistence(timeout: 5))

        button(prefix: "command_delete_").tap()
        XCTAssertTrue(app.staticTexts["Delete command"].waitForExistence(timeout: 5))
        app.buttons["dialog_confirm_button"].tap()
        XCTAssertTrue(
            waitForDisappearance(app.staticTexts["docker ps --format \"table {{.Names}}\""], timeout: 5)
        )

        button(prefix: "command_delete_").tap()
        XCTAssertTrue(app.staticTexts["Delete command"].waitForExistence(timeout: 5))
        app.buttons["dialog_confirm_button"].tap()
        XCTAssertTrue(
            waitForDisappearance(app.staticTexts["docker system prune -a --volumes"], timeout: 5)
        )

        button(prefix: "tab_delete_").tap()
        XCTAssertTrue(app.staticTexts["Delete tab"].waitForExistence(timeout: 5))
        app.buttons["dialog_confirm_button"].tap()
        XCTAssertTrue(app.staticTexts["Add first tab"].waitForExistence(timeout: 5))
    }

    private func attachScreenshot(_ name: String) {
        let attachment = XCTAttachment(screenshot: app.screenshot())
        attachment.name = name
        attachment.lifetime = .keepAlways
        add(attachment)
    }

    private func field(_ identifier: String) -> XCUIElement {
        app.descendants(matching: .any)
            .matching(identifier: identifier)
            .firstMatch
    }

    private func field(prefix: String) -> XCUIElement {
        app.descendants(matching: .any)
            .matching(NSPredicate(format: "identifier BEGINSWITH %@", prefix))
            .firstMatch
    }

    private func button(prefix: String) -> XCUIElement {
        app.buttons
            .matching(NSPredicate(format: "identifier BEGINSWITH %@", prefix))
            .firstMatch
    }

    private func clearButton(prefix: String) -> XCUIElement {
        app.buttons
            .matching(
                NSPredicate(
                    format: "identifier BEGINSWITH %@ AND identifier ENDSWITH '_clear'",
                    prefix
                )
            )
            .firstMatch
    }

    private func type(_ text: String, into element: XCUIElement) {
        XCTAssertTrue(element.waitForExistence(timeout: 5))
        element.tap()
        element.typeText(text)
    }

    private func clear(_ element: XCUIElement) {
        element.coordinate(withNormalizedOffset: CGVector(dx: 0.95, dy: 0.5)).tap()
        element.typeText(String(repeating: XCUIKeyboardKey.delete.rawValue, count: 64))
    }

    private func waitForDisappearance(_ element: XCUIElement, timeout: TimeInterval) -> Bool {
        let predicate = NSPredicate(format: "exists == false")
        let expectation = XCTNSPredicateExpectation(predicate: predicate, object: element)
        return XCTWaiter().wait(for: [expectation], timeout: timeout) == .completed
    }
}

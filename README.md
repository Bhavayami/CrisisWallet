# 💰 CrisisWallet — Economic Crisis & Personal Budget Simulator

**Understand how an economic crisis affects your wallet, expenses, and savings.**

CrisisWallet is a Java-based desktop application designed to simulate how economic crises and rising prices can affect an individual's monthly budget and financial stability.

A crisis does not affect just one product. Rising fuel prices can increase transportation and delivery costs, which may contribute to higher grocery and everyday living expenses. When expenses increase while income remains the same, people may have less money available for savings and emergencies.

CrisisWallet connects these economic changes to personal finances. Users can enter their financial details, simulate different crisis scenarios, analyze changes in their expenses, explore budgeting strategies, and generate a final financial report.

---

## 🌍 1. Real-World Problem

Economic crises can affect ordinary people through inflation, supply-chain disruptions, fuel shortages, changing taxes, and increasing prices of essential goods.

Consider a student or a family with a fixed monthly budget. They may plan their spending carefully, but an unexpected increase in transportation, food, electricity, or other essential costs can disturb that plan.

For example:

- Petrol prices increase, making commuting more expensive.
- Transportation and delivery costs rise, potentially affecting grocery prices.
- Food and essential goods become more expensive.
- More money goes toward monthly expenses.
- Less money remains for savings and emergencies.
- If the crisis continues, financial pressure may increase further.

The problem is not simply that prices rise. It is that **multiple economic changes can interact and affect an individual's ability to spend, save, and manage emergencies.**

Traditional budgeting tells people where their money goes. CrisisWallet goes a step further by helping them explore what could happen to their budget under different crisis conditions.

## 💡 2. Proposed Solution

CrisisWallet provides a simulated financial environment in which users can examine the possible effects of an economic crisis on their personal finances.

The application uses user-provided financial information and crisis-impact assumptions to calculate changes in expenses and savings. It presents the results in an understandable graphical interface and helps users explore possible budgeting decisions.

The project combines two ideas:

1. **Economic impact analysis:** Understanding how a crisis can affect prices, expenses, and financial conditions.
2. **Personal budget management:** Understanding how those changes influence an individual's monthly spending, savings, emergency preparedness, and budgeting choices.

The goal is to make economic changes easier to understand at an individual level.

## 🎯 3. Project Objectives

- Simulate the possible effects of economic crises on personal finances.
- Analyze how different expense categories respond to changing prices.
- Calculate the revised monthly expenditure under simulated conditions.
- Understand the relationship between expenses, income, and savings.
- Examine emergency fund adequacy and financial survival under different assumptions.
- Identify flexible expenses that may be reduced to manage a tighter budget.
- Compare different crisis scenarios.
- Present financial calculations through a user-friendly Java GUI.
- Generate a consolidated report of the simulation results.

## ✨ 4. Key Features

### 👤 Personal Financial Setup
Enter financial details such as monthly income, available savings, and expense amounts to establish a starting budget.

### 🌎 Crisis Simulation
Explore available crisis scenarios and, where supported by the application, create a custom crisis with user-defined impact assumptions.

### 📊 Expense Impact Analysis
Calculate how simulated price changes affect different spending categories and the overall monthly budget.

### 📈 Personal Inflation Analysis
Understand how the user's own spending pattern influences the effect of rising prices on their budget.

### 💸 Savings Analysis
Examine how changes in income and expenditure influence the money remaining for savings.

### 🚨 Emergency Fund Analysis
Estimate whether available savings can support expenses during a financial emergency.

### 🛟 Survival Mode
Explore how long available savings could cover expenses if income is interrupted, based on the application's calculations and assumptions.

### 🧩 Budget Rescue Plan
Identify areas where spending may be reduced to help manage increased costs without treating every expense as equally flexible.

### 🔄 Crisis Comparison
Compare the simulated effects of different crisis scenarios on personal finances.

### 🔮 Financial Projection
Explore potential future financial outcomes if the selected conditions continue over a specified period.

### 📄 Final Financial Report
Review a consolidated summary of the simulated financial impact, expense changes, savings, and budgeting insights.

*Note: The features described above should correspond to the modules present in the version uploaded to this repository.*

## 🔗 5. Economic Chain Reaction

CrisisWallet is based on the idea that an economic crisis can produce effects beyond its initial cause.

```text
          ECONOMIC CRISIS
                 |
                 v
        Supply Disruptions
                 |
                 v
      Raw Material Costs Rise
                 |
                 v
      Production Costs Rise
                 |
                 v
       Product Prices Rise
                 |
                 v
      Household Expenses Rise
                 |
                 v
         Savings Decrease
                 |
                 v
      Financial Pressure Rises
```

This is a simplified illustration, not a universal outcome. The actual effects depend on the type of crisis, the affected industries, government policies, market conditions, and individual spending habits.

CrisisWallet focuses on the personal-budget side of this chain by simulating how changing costs may affect a user's finances.

## ⚙️ 6. How the Application Works

The application follows a user-driven process:

1. **Enter financial details:** Provide the income, savings, and expense values requested by the application.
2. **Establish the normal budget:** Calculate the starting financial position.
3. **Select a crisis:** Choose a supported scenario or configure a custom scenario if available.
4. **Simulate the impact:** Apply the configured changes to relevant expense categories.
5. **Analyze the results:** Examine revised expenses, remaining money, and savings impact.
6. **Explore financial tools:** Use the available emergency fund, survival, projection, comparison, or budget rescue modules.
7. **Generate the final report:** Review the overall outcome of the simulation.

This approach makes the relationship between economic changes and personal financial decisions easier to understand.

## 🧪 7. Example Real-World Scenario

Imagine a student with the following illustrative financial situation:

| Financial detail | Example value |
|---|---:|
| Monthly income or allowance | ₹20,000 |
| Available savings | ₹40,000 |
| Monthly expenses | Entered by the user |

The student spends money on food, transportation, mobile recharge, education, shopping, and entertainment.

Suppose a simulated supply-chain crisis increases transportation and grocery costs.

CrisisWallet can help the student investigate:

- How the simulated crisis changes monthly expenses.
- How much money remains after those expenses.
- How the revised budget affects savings.
- Whether available savings can cover an emergency.
- Which flexible spending categories could be reduced.
- How the final financial position compares with the normal budget.

The example values are for demonstration only. Results depend on the values and assumptions entered into the application.

## 🛠️ 8. Technologies Used

| Technology | Purpose |
|---|---|
| Java | Application logic and financial calculations |
| Java Swing | Desktop graphical user interface |
| Java event handling | Responding to button clicks and user actions |
| Java collections and data structures | Organizing and processing financial and crisis data, where used |
| Java file handling | Saving or writing reports, where implemented |
| Java graphics APIs | Drawing custom charts, where implemented |

The application is intended as a Java academic project that demonstrates how programming concepts can be applied to a practical financial problem.

## ☕ 9. Java Concepts Used

### Object-Oriented Programming

**Classes and Objects**

Classes represent entities such as users, crises, calculators, and results. Objects hold data and perform operations defined by their classes.

**Encapsulation**

Data and related operations can be grouped within classes, helping organize the program and control access to internal state.

**Inheritance**

A common crisis structure can be extended to represent different types of crises while reusing shared functionality.

**Abstraction**

Common operations can be described through abstract classes or interfaces so that the implementation can work with a general concept rather than depend on every specific implementation.

**Polymorphism**

Different crisis implementations can be processed through a common parent type or interface, allowing the program to use the appropriate behavior for each scenario.

**Method Overriding**

A subclass can provide its own implementation of a method defined by a parent class when a particular crisis requires different behavior.

**Constructors**

Constructors initialize objects with the data required for their operation.

### Core Java Concepts

**Arrays and Collections**

Arrays and collection classes can store and process categories, scenario information, and calculation results. The particular collection types used should be confirmed from the source code.

**Exception Handling**

`try`, `catch`, and related exception-handling mechanisms help deal with invalid input and other runtime errors where implemented.

**Custom Exceptions**

A custom exception can represent a specific validation problem when the application defines and uses one.

**Interfaces**

Interfaces define common operations that implementing classes must provide.

**Packages**

Packages organize related classes into logical groups, making the source code easier to maintain.

**File Handling**

Java file-writing classes can be used to save generated reports or other application output.

### GUI Programming

**Java Swing**

Swing components provide the desktop interface, including windows, panels, labels, input fields, buttons, and other controls.

**Event Handling**

Event listeners connect user actions, such as pressing a button, to the appropriate program logic.

**Layout Managers**

Layout managers organize interface components into a structured and usable layout.

**Custom Graphics**

Java graphics APIs can be used to draw charts or other visual representations of financial results when implemented in the application.

**Multithreading**

Background tasks can be handled using Java threads or related mechanisms where implemented, helping separate longer-running operations from normal interface interactions.

*The list above describes Java concepts relevant to the project. Keep only the concepts and APIs that are actually present in your final source code.*

## 🖥️ 10. Graphical User Interface

CrisisWallet uses a desktop interface to make financial analysis accessible without requiring users to perform every calculation manually.

The interface is intended to help users:

- Enter financial information.
- Choose or configure crisis scenarios.
- View financial calculations and comparisons.
- Navigate between budgeting tools.
- Understand the final simulation results.

## 🚀 11. Installation and Execution

### Prerequisites

- Java Development Kit (JDK).
- A Java-compatible IDE, such as IntelliJ IDEA, Eclipse, or NetBeans, or a terminal with Java tools.
- The source files included in this repository.


### Run the Application

1. Open the project in your Java IDE.
2. Locate the main class containing the `main()` method.
3. Ensure the source files are compiled with the required packages and dependencies.
4. Build and run the main class.
5. Enter the requested financial values.
6. Select a crisis scenario and explore the available analysis modules.

If the project has a specific folder structure or build configuration, follow the corresponding instructions in the repository.

## 📁 12. Project Structure

The project is organized around the main application, financial calculations, crisis handling, graphical interface, and report generation.

The exact structure depends on the source files included in the repository. A typical logical organization is:

```text
CrisisWallet/
├── Main.java
├── model/
├── crisis/
├── calculator/
├── gui/
├── report/
├── util/
└── README.md
```

This is an illustrative overview. Replace it with the exact folder and package structure of the uploaded source code if it differs.

## ✅ 13. Testing

The application can be evaluated using test cases such as:

| Test case | Expected behavior |
|---|---|
| Valid financial inputs | Accept values and calculate the budget |
| Invalid numeric input | Handle the input error appropriately |
| No crisis or baseline budget | Display the starting financial position |
| Crisis with increased expenses | Calculate the revised budget |
| Different crisis scenarios | Display the corresponding simulated results |
| Low available savings | Reflect the effect on emergency preparedness |
| Budget rescue analysis | Display the available recommendations or adjustments |
| Final report generation | Present the consolidated simulation results |

These are suggested test cases. Actual results should be verified by running the current application.

## 🌱 14. Future Enhancements

Possible future improvements include:

- Real-time economic data from reliable sources.
- Regional inflation and cost-of-living information.
- Additional crisis scenarios and configurable impact assumptions.
- More detailed graphical comparisons.
- PDF or spreadsheet report export.
- Personalized budgeting recommendations.
- More detailed emergency fund planning.
- Improved validation and automated testing.

These are future possibilities and are not claims that the features are already implemented.

## ⚠️ 15. Limitations

- The accuracy of the results depends on the values and assumptions provided by the user.
- Simulated price changes may differ from actual market conditions.
- Economic relationships are simplified for educational and budgeting purposes.
- Future projections are estimates based on assumptions, not guaranteed predictions.
- The application is not a substitute for professional financial advice.

## 🌟 16. What Makes CrisisWallet Different?

CrisisWallet connects an economic concept with a personal problem.

Instead of studying rising prices only as abstract economic figures, users can explore how changes in everyday expenses may affect their own financial position.

The project brings together:

- Economic crisis simulation.
- Personal budget analysis.
- Expense and savings calculations.
- Emergency preparedness.
- Budget management.
- Java GUI development and object-oriented programming.

Its central idea is simple: **understand how a crisis may affect your wallet before deciding how to manage your budget.**

## 🎓 17. Educational Significance

CrisisWallet demonstrates how Java can be used to build an application around a real-world problem. It combines user input, financial calculations, scenario-based processing, graphical interaction, and reporting in one project.

It also illustrates how programming can help transform complex information into results that ordinary users can understand and use for better planning.

## 👩‍💻 18. Author and Project Information

**Project Name:** CrisisWallet  
**Project Type:** Java desktop application  
**Domain:** Personal Finance, Budgeting, and Economic Crisis Simulation  
**Purpose:** Academic and educational project

## 📜 19. License

No license has been specified. If you want others to reuse, modify, or distribute this project, choose an appropriate open-source license and add its license file to the repository.

---

**CrisisWallet — Because a crisis may be out of your control, but understanding its impact on your budget can help you prepare.**


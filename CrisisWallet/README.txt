CRISISWALLET - Personal Financial Crisis Impact Simulator (Java Swing)
=======================================================================

HOW TO RUN
  Windows : double-click run.bat
  Linux/Mac: ./run.sh
  Manual  : (from this folder)
            mkdir out
            javac -d out src/crisiswallet/*.java src/crisiswallet/*/*.java
            java -cp out crisiswallet.Main
  Class path: "out" is the class path (-cp out). Package crisiswallet.Main is the entry point.

HOW TO USE
  Home screen: six boxes (GridLayout 2x3). Only box 1 is open at first; after you run a simulation the
  other five unlock. Click a box to open it, use "< Back to Home" (or press Esc) to return.
  1. Setup tab    : click "Fill sample data" (or type your own), choose a crisis, optional salary cut, click "Simulate crisis".
  2. Impact tab   : before/during table, personal inflation, extra monthly/yearly cost, biggest impact, bar chart.
  3. Savings tab  : savings before/during, extra income needed, emergency fund coverage (progress bar).
  4. Projection   : 3/6/12/24 month savings line chart; "Animate projection" draws it month by month (thread).
  5. Compare tab  : all crises compared and ranked (one thread per crisis).
  6. Final Report : full text report, can be saved as .txt.

CRISES (real events; percentages are scenario ASSUMPTIONS, not exact historical data)
  COVID-19 (2020), Russia-Ukraine war (2022), Middle East conflict and regional
  instability (2023 onwards), LPG cooking gas shortage (2026), plus Custom Crisis.

PROJECT STRUCTURE (root package + 7 sub-packages)
  crisiswallet            Main
  crisiswallet.model      User, InvalidInputException
  crisiswallet.crisis     Crisis, ScenarioCrisis, CovidCrisis, RussiaUkraineCrisis,
                          MiddleEastCrisis, LpgShortageCrisis, CustomCrisis
  crisiswallet.calculator Calculator, ImpactCalculator, SavingsCalculator,
                          EmergencyFundCalculator, ProjectionCalculator, CrisisResult
  crisiswallet.task       ProjectionAnimator, CompareWorker, AnimationListener (threads)
  crisiswallet.report     ReportGenerator
  crisiswallet.util       Displayable, Reportable, MoneyFormat
  crisiswallet.gui        MainFrame, SetupPanel, ImpactPanel, SavingsPanel, ProjectionPanel,
                          ComparePanel, ReportPanel, DashboardPanel, MenuCard, StatCard, BarChartPanel, LineChartPanel

SYLLABUS MAPPING (for viva)
  Unit 1 Primitives & objects ......... double/int/String/boolean, objects created with new everywhere
  Unit 2 Loops & control .............. for / for-each / while, if-else, (no switch used)
  Unit 3 Access modifiers ............. private fields (User), protected (Crisis, Calculator), default
                                        (ReportGenerator.line()), public methods
  Unit 3 Constructors ................. User() default constructor calling this(0,0) (constructor invocation)
  Unit 3 static ....................... User.CATEGORIES, User.userCount, ReportGenerator, MoneyFormat
  Unit 3 abstract ..................... Crisis, Calculator (abstract methods)
  Unit 3 final ........................ Crisis.getName(), final class LpgShortageCrisis, final class
                                        ReportGenerator/MoneyFormat, static final CATEGORIES
  Unit 4 Object class ................. toString() overridden in User and Crisis
  Unit 4 super ........................ super(...) in every subclass constructor, super.describe()
  Unit 4 Multi-level inheritance ...... Crisis -> ScenarioCrisis -> CovidCrisis (and 3 more)
  Unit 4 Typecasting .................. List<Crisis> holds subclasses (upcast); (ScenarioCrisis) downcast
                                        after instanceof in SetupPanel.simulate()
  Unit 4 Overriding ................... describe(), getPercentages(), calculate(), toReportText(), paintComponent()
  Unit 4 Final & abstract classes ..... see above
  Unit 5 1D arrays .................... User.CATEGORIES, expenseFields[], MONTH_OPTIONS[], double[] percent
  Unit 5 2D arrays .................... ProjectionCalculator.table = new double[months+1][3]
  Unit 5 Inheritance in arrays ........ List/array of Crisis objects holding different subclasses
  Unit 5 Checked exception ............ InvalidInputException (extends Exception), IOException when saving
  Unit 5 Unchecked exception .......... NumberFormatException, IllegalArgumentException
  Unit 5 try/catch/throw/finally ...... SetupPanel.parse() and simulate(); ReportGenerator.saveToFile()
  Unit 5 Packages & imports ........... 8 packages (incl. root); single-class import, wildcard import (crisiswallet.calculator.*)
  Unit 5 Class path ................... javac -d out ... / java -cp out crisiswallet.Main
  Unit 5 Inheritance in packages ...... subclasses in other packages (e.g. gui panels extend JPanel,
                                        MainFrame extends JFrame, calculators extend Calculator)
  Unit 5 Interfaces / multiple
         inheritance using interfaces . Calculator implements Displayable AND Reportable;
                                        ProjectionPanel implements ActionListener AND AnimationListener
  Unit 6 Swing ........................ JFrame, CardLayout (home screen + pages), JTable, JComboBox, JTextField, JProgressBar ...
  Unit 6 Layout managers .............. GridLayout on every page + home screen: DashboardPanel (2x3 boxes), SetupPanel (1x2 and 0x2 forms), ImpactPanel (1x4 cards,
                                        2x1, 1x2), SavingsPanel (1x5 cards), ProjectionPanel (1x3, 1x4, 2x1),
                                        ComparePanel (1x2, 1x3, 2x1), ReportPanel (1x3); StatCard is the reusable box
  Unit 6 Graphics ..................... BarChartPanel and LineChartPanel (paintComponent, Graphics2D)
  Unit 6 Collections .................. ArrayList (crises, workers), LinkedHashMap/HashMap (expenses,
                                        percentages), LinkedHashSet (categories that rose),
                                        Collections.sort + Comparable (CrisisResult), synchronizedList
  Unit 6 Multithreading ............... ProjectionAnimator (Runnable + Thread.sleep),
                                        CompareWorker (extends Thread, start, join),
                                        volatile stop flag, SwingUtilities.invokeLater

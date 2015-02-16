Team PIK
========

Assessment 3 Project Code

Major Modifications
===================

1. Fantastic sound effects and background music added
2. Better scoring - points are awarded relative to distance between stations
3. Improved train allocation
4. Improved goal allocation - now players will only receive completable goals
5. Greated range of goals possible, including via stations
6. Acronyms added to the map to help identify cities
7. Train route displayed on map if train is selected
8. Major GUI overhaul - now trains, goals and score are displayed better and an instruction screen from the main menu has been added
9. Changeable game length
10. Junction failure on train collision
11. Border control - a chance for players to have their trains removed
12. Special stations which increase/decrease train speed
13. Added methods for rapidly finding the actual distance between stations

Project Set-up Instructions
===========================

1. Clone the repository.  It helps.  Don't worry about your project files overwriting any other files, Git has been configured to ignore these.  If you're using the GitHub tools, also install good old fashioned Git as it integrated with both Eclipse and IntelliJ IDEA to make managing version control easier.
2. If you will be using eclipse, set your workspace to be the repository root. You should also install the Gradle IDE from http://marketplace.eclipse.org/content/gradle-ide-pack, or you can install it from the eclipse marketplace, or using this handy install button.
<a href="http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=1640500" class="drag" title="Drag to your running Eclipse workspace to install Gradle IDE Pack"><img src="http://marketplace.eclipse.org/sites/all/themes/solstice/_themes/solstice_marketplace/public/images/btn-install.png" alt="Drag to your running Eclipse workspace to install Gradle IDE Pack" /></a>
3. In Eclipse go to File -> Import -> Gradle -> Gradle Project.  Choose the taxe folder in the repo and then press Build Model.  You should import both Core and Desktop, the taxe project is just a necessary wrapper and will never be used for anything.  Click Import and wait patiently.
4. To set up IntelliJ IDEA, clone the repository into IDEA projects folder (I used terminal for that). Now open up IDEA and select that you'll import a project from a file/folder. Select build.gradle file from FVS2/taxe/core folder. Now IDEA does some importing and downloading.
To setup run environment:
Click Run -> Edit configurations...
Click on + in upper left.
Select Application and use Desktop for the name.
Select ../FVS2/taxe/core/assets as the Working directory
Use classpath of module: desktop
Main class: uk.ac.york.cs.sepr.fvs.taxe.desktop.DesktopLauncher
And it's ready.
You can try clicking on Run now and you should see the sample program work :)
5. Setting up and using Git on IDEA is really intuitive.

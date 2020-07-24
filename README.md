# Umzugsplaner
adb shell setprop persist.sys.language de; setprop persist.sys.country de; stop; sleep 5; start

# monkey test
adb shell monkey -p com.planner.generic.checklist -v 1000
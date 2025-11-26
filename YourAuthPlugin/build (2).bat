@echo off
chcp 65001
echo ===============================
echo   کامپایل پلاگین ماینکرفت
echo ===============================

echo.
echo 🔹 بررسی نصب JDK...
javac -version >nul 2>&1
if errorlevel 1 (
    echo ❌ JDK نصب نیست!
    echo 📥 لطفا JDK 17 رو نصب کن
    pause
    exit
)

echo.
echo 🔹 مرحله 1: بررسی فایل‌ها...
if not exist "src\main\java\com\yourname\authplugin\Main.java" (
    echo ❌ فایل Main.java پیدا نشد!
    pause
    exit
)

echo.
echo 🔹 مرحله 2: دانلود paper-api...
if not exist "paper-api-1.19.4.jar" (
    echo 📥 در حال دانلود paper-api...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.papermc.io/repository/maven-public/io/papermc/paper/paper-api/1.19.4-R0.1-SNAPSHOT/paper-api-1.19.4-R0.1-SNAPSHOT.jar' -OutFile 'paper-api-1.19.4.jar'"
)

echo.
echo 🔹 مرحله 3: ایجاد پوشه خروجی...
if not exist "output" mkdir output

echo.
echo 🔹 مرحله 4: کامپایل کدها...
javac -cp "paper-api-1.19.4.jar" -d output ^
    src\main\java\com\yourname\authplugin\Main.java ^
    src\main\java\com\yourname\authplugin\AuthManager.java ^
    src\main\java\com\yourname\authplugin\commands\LoginCommand.java ^
    src\main\java\com\yourname\authplugin\commands\RegisterCommand.java ^
    src\main\java\com\yourname\authplugin\listeners\AuthListener.java

if errorlevel 1 (
    echo ❌ خطا در کامپایل!
    pause
    exit
)

echo.
echo 🔹 مرحله 5: کپی کردن plugin.yml...
if exist "src\main\resources\plugin.yml" (
    copy "src\main\resources\plugin.yml" "output\plugin.yml"
)

echo.
echo 🔹 مرحله 6: ساخت فایل JAR...
cd output
jar cf ../AuthPlugin-1.0.0.jar .
cd ..

echo.
echo ✅ کامپایل موفقیت آمیز بود!
echo 📁 فایل ساخته شده: AuthPlugin-1.0.0.jar
pause
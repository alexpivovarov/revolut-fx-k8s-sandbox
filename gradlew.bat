@rem Ensures a specific Gradle version runs, even if Gradle isn’t installed.
@rem Finds Java, sets minimal JVM options, boots the Gradle Wrapper bootstrap, downloads the pinned Gradle distribution if necessary, then forwards your command (e.g., build, test) to that Gradle.
@rem step-by-step execution flow
@rem 1. Shell setup and echo control
@rem 2. Local scope and environment hygiene
@rem 3. Resolve script location and app metadata
@rem 4. Default JVM options
@rem 5. Locate a Java runtime
@rem 6. Build the classpath for the bootstrap
@rem 7. Launch the Gradle Wrapper bootstrap
@rem 8. Bootstrap reads gradle/wrapper/gradle-wrapper.properties ; resolves the distribution
@rem 9. Exit code handling



@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows
@rem  Purpose: Launches the Gradle Wrapper, ensuring a consistent Gradle
@rem           version is used without requiring Gradle to be preinstalled.
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem %~dp0 expands to the directory of this script (ending with a backslash)
set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem APP_BASE_NAME is the script base name; rarely used but sets a system prop later
set APP_BASE_NAME=%~n0
@rem APP_HOME starts as the script directory; used to locate the wrapper JAR
set APP_HOME=%DIRNAME%

@rem Resolve any "." and ".." in APP_HOME to make it shorter and absolute.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Default JVM options for running the small bootstrap JVM.
@rem Keep these modest to avoid starving the wrapper process.
@rem Can be overridden/augmented via JAVA_OPTS or GRADLE_OPTS env vars.
set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

@rem Locate Java. Prefer JAVA_HOME if set; otherwise fall back to PATH.
@rem Robust Java discovery is critical because the wrapper is a Java program.
@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

@rem If we reach here, Java wasn't found in PATH and JAVA_HOME is unset.
echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:findJavaFromJavaHome
@rem Strip quotes and construct the expected java.exe path under JAVA_HOME
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

@rem Validate that the computed java.exe actually exists
if exist "%JAVA_EXE%" goto execute

@rem JAVA_HOME was set but invalid; guide the user to fix it
echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:execute
@rem Setup the command line
@rem CLASSPATH must include the Gradle Wrapper bootstrap JAR packaged in the repo
set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

@rem Execute Gradle via the wrapper main class.
@rem - DEFAULT_JVM_OPTS / JAVA_OPTS / GRADLE_OPTS: JVM options aggregation
@rem -Dorg.gradle.appname: lets Gradle know which script invoked it (logging)
@rem -classpath: points to wrapper JAR so the bootstrap can run
@rem org.gradle.wrapper.GradleWrapperMain: downloads + runs pinned Gradle
@rem %*: forwards all user-provided args (e.g., "build", "test") to Gradle
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

:end
@rem End local scope for the variables with windows NT shell
@rem If the java process exited cleanly, jump to mainEnd; otherwise fall through to :fail
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
@rem Normalize a nonzero exit code and propagate it in a shell-friendly way.
@rem Set variable GRADLE_EXIT_CONSOLE if you need the _script_ return code instead of
@rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%GRADLE_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
@rem Restore environment if we used setlocal above
if "%OS%"=="Windows_NT" endlocal

:omega
@rem Script end label

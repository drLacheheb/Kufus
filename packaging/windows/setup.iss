; -----------------------------------------------------------------------------
; Kufus - Modern Dark Setup Wizard Script (Inno Setup 6)
; -----------------------------------------------------------------------------

#define MyAppName "Kufus"
#ifndef MyAppVersion
#define MyAppVersion "1.0.0"
#endif
#define MyAppPublisher "drLacheheb"
#define MyAppURL "https://github.com/drLacheheb/Kufus"
#define MyAppExeName "Kufus.exe"

[Setup]
; Unique application GUID matching MSI upgradeUuid for clean upgrades/uninstalls
AppId={{9D3E6B4A-4C1F-4F2B-9A82-3D7E6E51A104}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppVerName={#MyAppName} v{#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={autopf}\{#MyAppName}
UninstallDisplayIcon={app}\{#MyAppExeName}
DisableProgramGroupPage=yes
ArchitecturesAllowed=x64compatible
ArchitecturesInstallIn64BitMode=x64compatible
LicenseFile=..\..\LICENSE

; Modern Dark Theme UI Styling
WizardStyle=modern dark

; Allow installation for both regular users and admins
PrivilegesRequired=lowest
PrivilegesRequiredOverridesAllowed=dialog

; Seamless Upgrade and In-Place Update Settings
UsePreviousAppDir=yes
UsePreviousTasks=yes
CloseApplications=yes
RestartApplications=yes

; Output configuration
OutputBaseFilename=Kufus-Setup-{#MyAppVersion}
OutputDir=..\..\desktopApp\build\compose\binaries\main-release\setup
Compression=lzma2/ultra64
SolidCompression=yes
SetupIconFile=kufus.ico
WizardSmallImageFile=wizard_small.bmp

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: checkedonce
Name: "startmenuicon"; Description: "Create a Start Menu shortcut"; GroupDescription: "{cm:AdditionalIcons}"; Flags: checkedonce

[Files]
Source: "..\..\desktopApp\build\compose\binaries\main-release\app\Kufus\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: startmenuicon
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

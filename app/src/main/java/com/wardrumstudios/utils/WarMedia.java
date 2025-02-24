package com.wardrumstudios.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Process;
import android.os.Vibrator;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;


import com.nvidia.devtech.NvUtil;
import com.rockstargames.gtasa.BuildConfig;
import com.rockstargames.gtasa.R;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class WarMedia extends WarGamepad {
    static final int REQUEST_READ_EXTERNAL_STORAGE = 8001;
    static final int REQUEST_RECORD_AUDIO = 1;
    public String APPLICATION_ID = BuildConfig.APPLICATION_ID;
    public boolean AddMovieExtension;
    protected boolean AllowLongPressForExit;
    String DeviceCountry;
    public String DeviceLocale;
    boolean DisplayMovieTextOnTap;
    boolean DoLog;
    boolean ForceSize = false;
    boolean GameIsFocused;
    protected int HELLO_ID;
    boolean IsInValidation;
    private boolean IsScreenPaused = false;
    int IsShowingBackMessage;
    boolean KeepAspectRatio = true;
    boolean MovieIsSkippable;
    boolean MovieTextDisplayed;
    protected int SpecialBuildType;
    protected boolean UseExpansionFiles;
    protected boolean UseFTPDownload = false;
    public boolean UseWarDownloader = true;
    boolean UsingSounds;
    Activity activity;
    protected String apkFileName = "";
    protected CharSequence appContentText;
    protected CharSequence appContentTitle;
    protected Intent appIntent;
    protected int appStatusIcon;
    protected CharSequence appTickerText;
    AudioManager audioManager;
    int audioMax;
    int audioVolume;
    int availableMemory;
    int bIsPlayingMovie;
    boolean bMoviePlayerPaused;
    public String baseDirectory;
    public String baseDirectoryRoot;
    protected int baseDisplayHeight;
    protected int baseDisplayWidth;
    protected int cachedSizeRead;
    protected boolean checkForMaxDisplaySize;
    LinearLayout col1;
    LinearLayout col2;
    LinearLayout col3;
    String currentMovieFilename;
    int currentMovieLength;
    int currentMovieMS;
    int currentMovieOffset;
    float currentMovieVolume;
    int currentTempID;
    SurfaceHolder customMovieHolder;
    SurfaceView customMovieSurface;
    public MediaPlayer dialogPlayer;
    SurfaceHolder downloadHolder;
    SurfaceView downloadView;
    boolean downloadViewCreated;
    AlertDialog exitDialog;
    protected String expansionFileName = "";
    protected boolean hasTouchScreen;
    boolean isCompleting;
    boolean isPhone;
    private boolean isUserPresent = true;
    long lastMovieStop;
    protected int lastNetworkAvailable;
    public LinearLayout llSplashView;
    public String localIp;
    private Locale locale;
    int mAscent;
    private TextView mAverageSpeed;
    Camera mCamera;
    private boolean mCancelValidation;
    private View mCellMessage;
    private View mDashboard;
    private ProgressBar mPB;
    private Button mPauseButton;
    private TextView mProgressFraction;
    private TextView mProgressPercent;
    private final BroadcastReceiver mReceiver;
    private int mState;
    private boolean mStatePaused;
    private TextView mStatusText;
    Paint mTextPaint;
    private TextView mTimeRemaining;
    private WifiManager mWifiManager;
    ActivityManager.MemoryInfo memInfo;
    int memoryThreshold;
    protected DisplayMetrics metrics;
    ActivityManager mgr;
    int movieLooping;
    public MediaPlayer moviePlayer;
    String movieSubtitleText;
    boolean movieTextDisplayNow;
    SurfaceHolder movieTextHolder;
    int movieTextScale;
    SurfaceView movieTextSurface;
    TextView movieTextView;
    LinearLayout movieView;
    boolean movieViewCreated;
    int movieViewHeight;
    boolean movieViewIsActive;
    TextView movieViewText;
    int movieViewWidth;
    int movieViewX;
    int movieViewY;
    SurfaceHolder movieWindowHolder;
    SurfaceView movieWindowSurface;
    public MediaPlayer musicPlayer;
    int[] myPid;
    private Vibrator myVib;
    private PowerManager.WakeLock myWakeLock;
    protected boolean overrideExpansionName = false;
    protected String patchFileName = "";
    protected byte[] privateData;
    protected String[] privateDataFiles;
    LinearLayout row1;
    LinearLayout row2;
    LinearLayout row3;
    TextPaint sTextPaint;
    float screenWidthInches;
    boolean skipMovies;
    boolean skipSound;
    boolean soundLog;
    ArrayList<PoolInfo> sounds;
    Paint tPaint;
    TextPaint textPaint;
    int totalMemory;
    long[][] vibrateEffects;
    public boolean waitForPermissions = false;
    public XAPKFile[] xAPKS = null;

    private native void initTouchSense(Context context);
    public native void NativeNotifyNetworkChange(int i);
    public native void setTouchSenseFilepath(String str);

    public WarMedia() {
        this.DoLog = !this.FinalRelease;
        this.moviePlayer = null;
        this.skipSound = false;
        this.skipMovies = false;
        this.isCompleting = false;
        this.bIsPlayingMovie = 0;
        this.soundLog = false;
        this.DisplayMovieTextOnTap = false;
        this.movieSubtitleText = "";
        this.movieTextDisplayNow = false;
        this.SpecialBuildType = 0;
        this.activity = null;
        this.HELLO_ID = 12346;
        this.appStatusIcon = 0;
        this.appTickerText = "MyApp";
        this.appContentTitle = "MyApp";
        this.appContentText = "Running - Return to Game?";
        this.appIntent = null;
        this.UseExpansionFiles = false;
        this.AllowLongPressForExit = false;
        this.hasTouchScreen = true;
        this.isPhone = false;
        this.currentTempID = 100000;
        this.baseDirectory = Environment.getExternalStorageDirectory().getPath();
        this.baseDirectoryRoot = Environment.getExternalStorageDirectory().getPath();
        this.AddMovieExtension = true;
        this.IsShowingBackMessage = 0;
        this.exitDialog = null;
        this.cachedSizeRead = 0;
        this.UsingSounds = false;
        this.memoryThreshold = 0;
        this.availableMemory = 0;
        this.totalMemory = 0;
        this.screenWidthInches = 0.0f;
        this.baseDisplayWidth = 1920;
        this.baseDisplayHeight = 1080;
        this.lastNetworkAvailable = -1;
        this.checkForMaxDisplaySize = false;
        this.mWifiManager = null;
        this.downloadViewCreated = false;
        this.GameIsFocused = false;
        this.mReceiver = new BroadcastReceiver() {
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass9 */

            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (!WarMedia.this.FinalRelease) {
                    System.out.println("BroadcastReceiver WarMedia " + action);
                }

                if (action.equals("android.intent.action.SCREEN_OFF")) {
                    if (!WarMedia.this.FinalRelease) {
                        System.out.println("BroadcastReceiver ACTION_SCREEN_OFF");
                    }
                    WarMedia.this.isUserPresent = false;
                } else if (action.equals("android.intent.action.USER_PRESENT") || action.equals("android.intent.action.SCREEN_ON")) {
                    if (!WarMedia.this.FinalRelease) {
                        System.out.println("BroadcastReceiver " + action);
                    }

                    KeyguardManager keyguardManager = (KeyguardManager) WarMedia.this.getSystemService(Context.KEYGUARD_SERVICE);
                    if (!WarMedia.this.FinalRelease) {
                        System.out.println("inKeyguardRestrictedInputMode " + keyguardManager.inKeyguardRestrictedInputMode());
                    }

                    if (!keyguardManager.inKeyguardRestrictedInputMode()) {
                        WarMedia.this.isUserPresent = true;
                        if (WarMedia.this.IsScreenPaused) {
                            WarMedia.this.IsScreenPaused = false;
                            if (WarMedia.this.viewIsActive) {
                                WarMedia.this.resumeEvent();
                                if (WarMedia.this.cachedSurfaceHolder != null) {
                                    WarMedia.this.cachedSurfaceHolder.setKeepScreenOn(true);
                                }
                            }

                            if (!WarMedia.this.paused) {
                                WarMedia.this.PauseMoviePlayer(false);
                            }
                        }
                    }
                } else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    WarMedia.this.NetworkChange();
                } else if (WarMedia.this.DoLog) {
                    System.out.println("Received " + action);
                }
            }
        };

        this.bMoviePlayerPaused = false;
        this.currentMovieMS = 0;
        this.currentMovieFilename = "";
        this.currentMovieOffset = 0;
        this.currentMovieLength = 0;
        this.currentMovieVolume = 0.5f;
        this.myVib = null;
        this.vibrateEffects = new long[][]{new long[]{0, 100, 100, 100, 100}, new long[]{0, 100, 50, 75, 100, 50, 100}, new long[]{0, 25, 50, 100, 50, 25, 100}, new long[]{0, 25, 50, 25, 100, 100, 100}, new long[]{0, 50, 50, 50, 50, 25, 100}};
        this.mgr = null;
        this.memInfo = null;
        this.myPid = null;
        this.MovieIsSkippable = false;
        this.lastMovieStop = 0;
        this.movieWindowSurface = null;
        this.movieWindowHolder = null;
        this.movieTextHolder = null;
        this.movieTextSurface = null;
        this.movieViewIsActive = false;
        this.movieViewCreated = false;
        this.customMovieHolder = null;
        this.customMovieSurface = null;
        this.movieViewWidth = 0;
        this.movieViewHeight = 0;
        this.movieViewX = 0;
        this.movieViewY = 0;
        this.movieLooping = 0;
        this.movieView = null;
        this.movieViewText = null;
        this.DeviceLocale = "";
        this.DeviceCountry = "";
        this.locale = null;
        this.IsInValidation = false;
        this.movieTextScale = 32;
        this.movieTextView = null;
        this.MovieTextDisplayed = false;
        this.llSplashView = null;
    }

    public static class XAPKFile {
        public final long mFileSize;
        public final int mFileVersion;
        public final boolean mIsMain;

        public XAPKFile(boolean isMain, int fileVersion, long fileSize) {
            mIsMain = isMain;
            mFileVersion = fileVersion;
            mFileSize = fileSize;
        }
    }

    class PoolInfo {
        float duration;
        String filename;
        float lv;
        float rv;
        int soundID;

        PoolInfo() {
            /* ~ */
        }
    }

    public void GetMaxDisplaySize() {
        if (Build.VERSION.SDK_INT >= 23) {
            Display defaultDisplay = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            System.out.println("width=" + point.x + " height=" + point.y);

            Display.Mode[] supportedModes = defaultDisplay.getSupportedModes();
            for (int i = 0; i < supportedModes.length; i++) {
                Display.Mode mode = supportedModes[i];

                System.out.println("mode " + i + " width=" + mode.getPhysicalWidth() + " height=" + mode.getPhysicalHeight());
            }
        }
    }

    @Override // com.wardrumstudios.utils.WarBilling, com.wardrumstudios.utils.WarGamepad, com.nvidia.devtech.NvEventQueueActivity, com.wardrumstudios.utils.WarBase
    public void onCreate(Bundle savedInstanceState) {

        apkFileName = GetPackageName(APPLICATION_ID);
        expansionFileName = "main.8." + APPLICATION_ID + ".obb";
        patchFileName = "patch.8." + APPLICATION_ID + ".obb";

        Helpers.resourceClassString = APPLICATION_ID + ".R";

        baseDirectory = GetGameBaseDirectory();

        xAPKS = new XAPKFile[2];
        xAPKS[0] = new XAPKFile(true, 8, 1967561852);
        xAPKS[1] = new XAPKFile(false, 8, 625313014);


        if (DoLog) {
            System.out.println("**** WarMedia onCreate");
        }

        ClearSystemNotification();

        metrics = getResources().getDisplayMetrics();
        System.out.println("Display Metrics Info:\n\tdensity:\t\t" + metrics.density + "\n\tdensityDPI:\t\t" + metrics.densityDpi + "\n\tscaledDensity:\t\t" + metrics.scaledDensity + "\n\twidthDPI:\t\t" + metrics.xdpi + "\n\theightDPI:\t\t" + metrics.ydpi + "\n\twidthPixels:\t\t" + metrics.widthPixels + "\n\theightPixels:\t\t" + metrics.heightPixels + "\n\tscreenlayout=" + getResources().getConfiguration().screenLayout);


        if (Build.MODEL.startsWith("ADT")) {
            IsAndroidTV = true;
        }

        if (Build.MANUFACTURER.startsWith("NVIDIA") && Build.MODEL.startsWith("SHIELD Android TV")) {
            if (checkForMaxDisplaySize) {
                GetMaxDisplaySize();
            }
        }

        NvUtil.getInstance().setActivity(this);
        NvUtil.getInstance().setAppLocalValue("STORAGE_ROOT", baseDirectory);
        NvUtil.getInstance().setAppLocalValue("STORAGE_ROOT_BASE", baseDirectoryRoot);

        hasTouchScreen = getResources().getConfiguration().touchscreen != 1;
        System.out.println("hastouchscreen " + hasTouchScreen + " touchscreen " + getResources().getConfiguration().touchscreen);

        activity = this;

        GetRealLocale();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        myWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "War:WarEngine");
        myWakeLock.setReferenceCounted(false);

        isPhone = IsPhone();
        screenWidthInches = ((float) metrics.widthPixels) / metrics.xdpi;
        if (isPhone) {
            if (screenWidthInches < 3.5f) {
                screenWidthInches = 3.5f;
            }

            if (screenWidthInches > 6.0f) {
                screenWidthInches = 6.0f;
            }
        } else {
            if (screenWidthInches < 6.0f) {
                screenWidthInches = 6.0f;
            }

            if (screenWidthInches > 10.0f) {
                screenWidthInches = 10.0f;
            }
        }

        setVolumeControlStream(3);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioMax = audioManager.getStreamMaxVolume(3);
        audioVolume = audioManager.getStreamVolume(3);
        System.out.println("availableProcessors " + Runtime.getRuntime().availableProcessors() + " cpu " + getNumberOfProcessors());

        GetMemoryInfo(true);


        localIp = getLocalIpAddress();

        if (!CustomLoadFunction()) {
            localHasGameData();
        }

        NetworkChange();

        try {
            initTouchSense(this);
        } catch (UnsatisfiedLinkError e) {
            /* ~ */
        }
        super.onCreate(savedInstanceState);
    }

    public boolean isTV() {
        return ((UiModeManager) getSystemService(Context.UI_MODE_SERVICE)).getCurrentModeType() == 4;
    }

    public boolean isWiFiAvailable() {
        if (mWifiManager == null) {
            mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }

        return mWifiManager != null && mWifiManager.isWifiEnabled();
    }

    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null) {
            return false;
        }

        return activeNetworkInfo.isConnected();
    }

    /* access modifiers changed from: package-private */
    private void ShowGPDownloadError() {
        Context context = this;
        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass1 */
        runOnUiThread(() -> {
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass1.AnonymousClass2 */
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass1.AnonymousClass1 */
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass1.AnonymousClass1.AnonymousClass1 */
            exitDialog = new AlertDialog.Builder(context).setTitle("Unknown download error. Please reinstall from Google Play").setPositiveButton("Quit Game", (i, a) -> finish()).setNegativeButton("Retry", (i, a) -> runOnUiThread(this::localHasGameData)).setCancelable(false).show();
            exitDialog.setCanceledOnTouchOutside(false);
        });
    }

    /* access modifiers changed from: package-private */
    private void ShowDownloadNetworkError() {
        Context context = this;
        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass2 */
        runOnUiThread(() -> {
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass2.AnonymousClass2 */
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass2.AnonymousClass1 */
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass2.AnonymousClass1.AnonymousClass1 */
            exitDialog = new AlertDialog.Builder(context).setTitle("No network connection. Cannot download game data.").setPositiveButton("Quit Game", (i, a) -> finish()).setNegativeButton("Continue", (i, a) -> runOnUiThread(this::localHasGameData)).setCancelable(false).show();
            exitDialog.setCanceledOnTouchOutside(false);
        });
    }

    public int downloadFTPFile(String filename, int filesize, boolean check) {
        return 0;
    }

    /* access modifiers changed from: protected */
    protected boolean localHasGameData() {
        if (xAPKS == null || expansionFilesDelivered()) {
            AfterDownloadFunction();
        } else {
            Toast.makeText(this, "no obb found", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public String GetGameBaseDirectory() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            try {
                File f = getExternalFilesDir(null);
                String base = f.getAbsolutePath();
                baseDirectoryRoot = base.substring(0, base.indexOf("/Android"));
                return (f.getAbsolutePath() + "/");
            } catch (NullPointerException e) {
                /* ~ */
            }
        }
        return "";
    }


    /* access modifiers changed from: package-private */
    protected boolean expansionFilesDelivered() {
        XAPKFile[] xAPKFileArr = this.xAPKS;
        for (XAPKFile xf : xAPKFileArr) {
            String fileName = Helpers.getExpansionAPKFileName(this, xf.mIsMain, xf.mFileVersion);
            if (!overrideExpansionName) {
                if (xf.mIsMain) {
                    expansionFileName = Helpers.generateSaveFileName(this, fileName);
                } else {
                    patchFileName = Helpers.generateSaveFileName(this, fileName);
                }

                System.out.println("download name " + (xf.mIsMain ? expansionFileName : patchFileName));
            }

            if (!Helpers.doesFileExist(this, fileName, xf.mFileSize, false)) {
                if (!FinalRelease) {
                    System.out.println("expansionFilesDelivered not exist " + fileName);
                }
                return false;
            }
        }
        return true;
    }

    private void initializeDownloadUI() {
        if (!FinalRelease) {
            System.out.println("initializeDownloadUI");
        }
        this.downloadView = new SurfaceView(this);
        this.downloadHolder = downloadView.getHolder();
        this.downloadHolder.addCallback(new SurfaceHolder.Callback() {
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass5 */

            public final void surfaceCreated(SurfaceHolder holder) {
                downloadViewCreated = true;
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (!FinalRelease) {
                    System.out.println("surfaceChanged called - subViewSplash");
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                if (!FinalRelease) {
                    System.out.println("surfaceDestroyed called - subViewSplash");
                }
            }
        });

//        downloadHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
        setContentView(downloadView);
        LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myParams.gravity = Gravity.CENTER;
        addContentView((LinearLayout) getLayoutInflater().inflate(Helpers.GetResourceIdentifier(getApplicationContext(), "download", "layout"), (ViewGroup) null), myParams);
        mPB = (ProgressBar) findViewById(Helpers.GetResourceIdentifier(getApplicationContext(), "progressBar", "id"));
        mStatusText = (TextView) findViewById(Helpers.GetResourceIdentifier(getApplicationContext(), "statusText", "id"));
        mProgressFraction = (TextView) findViewById(Helpers.GetResourceIdentifier(getApplicationContext(), "progressAsFraction", "id"));
        mProgressPercent = (TextView) findViewById(Helpers.GetResourceIdentifier(getApplicationContext(), "progressAsPercentage", "id"));
        mAverageSpeed = (TextView) findViewById(Helpers.GetResourceIdentifier(getApplicationContext(), "progressAverageSpeed", "id"));
        mTimeRemaining = (TextView) findViewById(Helpers.GetResourceIdentifier(getApplicationContext(), "progressTimeRemaining", "id"));
        mDashboard = findViewById(Helpers.GetResourceIdentifier(getApplicationContext(), "downloaderDashboard", "id"));
        mCellMessage = findViewById(Helpers.GetResourceIdentifier(getApplicationContext(), "approveCellular", "id"));
        mPauseButton = (Button) findViewById(Helpers.GetResourceIdentifier(getApplicationContext(), "pauseButton", "id"));
        Button mWiFiSettingsButton = (Button) findViewById(Helpers.GetResourceIdentifier(getApplicationContext(), "wifiSettingsButton", "id"));

        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass6 */
        this.mPauseButton.setOnClickListener(view -> {
            try {


                setButtonPausedState(!mStatePaused);
            } catch (Exception e) {
                System.out.println("mPauseButton error " + e.getMessage());
            }
        });

        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass7 */
        mWiFiSettingsButton.setOnClickListener(v -> {
            // ~
        });

        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass8 */
        ((Button) findViewById(Helpers.GetResourceIdentifier(getApplicationContext(), "resumeOverCellular", "id"))).setOnClickListener(view -> {

            mCellMessage.setVisibility(View.GONE);
        });
    }

    // 不需要暂停游戏
    public void onWindowFocusChanged(boolean hasFocus) {
        if (ResumeEventDone && isUserPresent && viewIsActive && !IsScreenPaused && !paused) {
            if (GameIsFocused && !hasFocus) {
                //pauseEvent();
            } else if (!GameIsFocused && hasFocus) {
                resumeEvent();
            }

            this.GameIsFocused = hasFocus;
        }

        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override // com.wardrumstudios.utils.WarGamepad
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        if (DoLog) {
            System.out.println("Listener - onConfigurationChanged orient " + newConfig.orientation + " " + newConfig);
        }



        super.onConfigurationChanged(newConfig);
    }

    public void onLowMemory() {
        lowMemoryEvent();
    }

    /* access modifiers changed from: protected */
    protected void NetworkChange() {
        int curNetwork = isWiFiAvailable() ? 2 : isNetworkAvailable() ? 1 : 0;
        if (curNetwork != lastNetworkAvailable) {
            NativeNotifyNetworkChange(curNetwork);
            lastNetworkAvailable = curNetwork;
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.USER_PRESENT");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.intent.action.SCREEN_ON");
        if (DoLog) {
            System.out.println("registerReceiver");
        }

        registerReceiver(mReceiver, filter);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (DoLog) {
            System.out.println("unregisterReceiver");
        }

        unregisterReceiver(mReceiver);
    }

    /* access modifiers changed from: protected */
    @Override // com.nvidia.devtech.NvEventQueueActivity, com.wardrumstudios.utils.WarBase
    public void onResume() {
        if (DoLog) {
            System.out.println("WarMedia**** onResume viewIsActive " + viewIsActive + " isUserPresent " + isUserPresent);
        }

        super.onResume();
        for (int i = 0; i < MAX_GAME_PADS; i++) {
            if (GamePads[i].active && GamePads[i].mogaController != null) {
                GamePads[i].mogaController.onResume();
            }
        }

        if (isUserPresent) {
            if (viewIsActive && ResumeEventDone) {
                resumeEvent();
                if (cachedSurfaceHolder != null) {
                    cachedSurfaceHolder.setKeepScreenOn(true);
                }
            }

            IsScreenPaused = false;
            PauseMoviePlayer(false);
        }

        this.paused = false;
    }

    /* access modifiers changed from: package-private */
    private void PauseMoviePlayer(final boolean bPause) {
        if (moviePlayer != null) {
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass10 */
            new Thread(() -> PauseMoviePlayerThread(bPause)).start();
        }
    }

    /* access modifiers changed from: package-private */
    private void PauseMoviePlayerThread(boolean bPause) {
        if (!skipMovies) {
            if (bPause) {
                try {
                    if (moviePlayer == null) {
                        return;
                    }

                    if (moviePlayer.isPlaying()) {
                        try {
                            currentMovieMS = moviePlayer.getCurrentPosition();
                            moviePlayer.pause();
                            bMoviePlayerPaused = true;
                            System.out.println("moviePlayer paused at " + currentMovieMS);
                        } catch (Exception ex) {
                            System.out.println("moviePlayer pause failed " + ex.getMessage());
                            try {
                                if (moviePlayer != null) {
                                    moviePlayer.release();
                                }
                            } catch (Exception e) {
                                /* ~ */
                            }

                            moviePlayer = null;
                            ClearVidView();
                            bMoviePlayerPaused = false;
                        }
                    } else {
                        System.out.println("moviePlayer not playing");
                        moviePlayer.release();
                        moviePlayer = null;
                        bMoviePlayerPaused = false;
                    }
                } catch (IllegalStateException e2) {
                    System.out.println("PauseMoviePlayerThread err " + e2.getMessage());
                    ClearVidView();
                    moviePlayer = null;
                    bIsPlayingMovie = 0;
                    bMoviePlayerPaused = false;
                }
            } else {
                System.out.println("moviePlayer resume bMoviePlayerPaused " + bMoviePlayerPaused + " moviePlayer " + moviePlayer);
                if (bMoviePlayerPaused && moviePlayer == null) {
                    if (this.currentMovieLength > 0) {
                        PlayMovieInFile(currentMovieFilename, 1.0f, currentMovieOffset, currentMovieLength);
                    } else {
                        PlayMovie(currentMovieFilename, 1.0f);
                    }

                    this.bMoviePlayerPaused = false;
                } else if (bMoviePlayerPaused) {
                    int count = 0;
                    while (!IsMovieViewActive()) {
                        if (!FinalRelease) {
                            System.out.println("moviePlayer waiting for vidViewIsActive");
                        }

                        mSleep(100);
                        count++;
                        if (count > 5) {
                            break;
                        }
                    }
                    if (count <= 5) {
                        try {
                            System.out.println("moviePlayer paused false");
                            if (currentMovieLength > 0) {
                                PlayMovieInFile(currentMovieFilename, 1.0f, currentMovieOffset, currentMovieLength, movieWindowHolder);
                            } else {
                                PlayMovie(currentMovieFilename, 1.0f);
                            }
                        } catch (Exception ex2) {
                            System.out.println("moviePlayer resume failed " + ex2.getMessage());
                            moviePlayer = null;
                            ClearVidView();
                        }
                    } else {
                        moviePlayer.release();
                        moviePlayer = null;
                        ClearVidView();
                    }

                    bMoviePlayerPaused = false;
                }
            }
        }
    }

    public void VibratePhone(int numMilliseconds) {
        if (!this.FinalRelease) {
            System.out.println("VibratePhone " + numMilliseconds);
        }

        if (this.myVib == null) {
            this.myVib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }

        if (this.myVib != null) {
            this.myVib.vibrate((long) numMilliseconds);
        }
    }

    public void VibratePhoneEffect(int effect) {
        if (!this.FinalRelease) {
            System.out.println("VibratePhoneEffect " + effect);
        }

        if (this.myVib == null) {
            this.myVib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }

        if (this.myVib != null) {
            this.myVib.vibrate(this.vibrateEffects[effect], -1);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.nvidia.devtech.NvEventQueueActivity, com.wardrumstudios.utils.WarBase
    public void onPause() {
        if (this.DoLog) {
            System.out.println("Listener -  onPause");
        }

        if (this.cachedSurfaceHolder != null) {
            this.cachedSurfaceHolder.setKeepScreenOn(false);
        }

        for (int i = 0; i < MAX_GAME_PADS; i++) {
            if (this.GamePads[i].active && this.GamePads[i].mogaController != null) {
                this.GamePads[i].mogaController.onPause();
            }
        }

        super.onPause();
        PauseMoviePlayer(true);
        GetMemoryInfo(true);
        this.IsScreenPaused = true;
        this.paused = true;
    }

    @Override // com.wardrumstudios.utils.WarGamepad, com.nvidia.devtech.NvEventQueueActivity
    public boolean onTouchEvent(MotionEvent event) {
        if (this.MovieIsSkippable) {
            StopMovie();
        }

        if (this.DisplayMovieTextOnTap) {
            this.movieTextDisplayNow = true;
            DrawMovieText();
        }

        if (this.IsShowingBackMessage != 2) {
            return super.onTouchEvent(event);
        }

        if (this.DoLog) {
            System.out.println("onTouchEvent exitDialog " + this.exitDialog);
        }

        if (this.exitDialog != null) {
            this.exitDialog.dismiss();
        }

        this.IsShowingBackMessage = 0;
        return true;
    }

    @Override // com.wardrumstudios.utils.WarGamepad, com.nvidia.devtech.NvEventQueueActivity
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!this.AllowLongPressForExit || keyCode != 4 || event.isAltPressed() || !event.isLongPress()) {
            return super.onKeyDown(keyCode, event);
        }

        this.IsShowingBackMessage = 1;
        if (this.DoLog) {
            System.out.println("ShowExitDialog KeyDown");
        }

        ShowExitDialog();
        return true;
    }

    /* access modifiers changed from: package-private */
    private void ShowExitDialog() {
        Context context = this;
        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass11 */
        this.handler.post(() -> {
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass11.AnonymousClass1 */
            WarMedia.this.exitDialog = new AlertDialog.Builder(context).setTitle("Press back again to exit").setOnKeyListener((dlg, KeyCode, event) -> {
                if (WarMedia.this.DoLog) {
                    System.out.println("ShowExitDialog onKey action " + event.getAction() + " IsShowingBackMessage " + WarMedia.this.IsShowingBackMessage + " KeyCode " + KeyCode);
                }

                if (WarMedia.this.IsShowingBackMessage == 2) {
                    WarMedia.this.IsShowingBackMessage = 0;
                    if (KeyCode == 4) {
                        WarMedia.this.finish();
                    } else {
                        dlg.dismiss();
                    }
                } else if (event.getAction() == 1) {
                    WarMedia.this.IsShowingBackMessage = 2;
                }
                return true;
            }).setCancelable(false).show();
            WarMedia.this.exitDialog.setCanceledOnTouchOutside(true);
        });
    }

    public String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = en.nextElement().getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("Got SocketException " + ex);
        }
        return null;
    }

    public String GetLocalIp() {
        return this.localIp;
    }

    /* access modifiers changed from: protected */
    @Override // com.wardrumstudios.utils.WarBase
    public void onStart() {

        super.onStart();
    }

    /* access modifiers changed from: protected */
    @Override // com.nvidia.devtech.NvEventQueueActivity, com.wardrumstudios.utils.WarBase
    public void onStop() {
        if (this.DoLog) {
            System.out.println("Listener - onStop");
        }

        super.onStop();
    }

    @Override // com.nvidia.devtech.NvEventQueueActivity
    public void onRestart() {
        if (this.DoLog) {
            System.out.println("Listener - onRestart");
        }

        super.onRestart();
    }

    @Override // com.wardrumstudios.utils.WarBilling, com.wardrumstudios.utils.WarGamepad, com.nvidia.devtech.NvEventQueueActivity, com.wardrumstudios.utils.WarBase
    public void onDestroy() {
        if (this.DoLog) {
            System.out.println("Listener - onDestroy isFinishing " + isFinishing());
        }

        Process.killProcess(Process.myPid());
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }

    public void finish() {
        onDestroy();
        super.finish();
    }

    public void ExitGame() {
        onDestroy();
        finish();
        Process.killProcess(Process.myPid());
    }

    /* access modifiers changed from: package-private */
    private int GetSoundsIndex(String filename) {
        for (int i = 0; i < this.sounds.size(); i++) {
            PoolInfo pi = this.sounds.get(i);
            if (pi.filename.equals(filename)) {
                return pi.soundID;
            }
        }
        return -1;
    }

    public int GetMemoryInfo(boolean allProcesses) {
        if (this.mgr == null) {
            this.mgr = (ActivityManager) super.getSystemService(Context.ACTIVITY_SERVICE);
            this.memInfo = new ActivityManager.MemoryInfo();
        }

        this.mgr.getMemoryInfo(this.memInfo);
        this.memoryThreshold = (int) (this.memInfo.threshold / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
        this.availableMemory = (int) ((this.memInfo.availMem / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
        if (Build.VERSION.SDK_INT >= 16) {
            this.totalMemory = (int) ((this.memInfo.totalMem / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
        } else {
            this.totalMemory = 256;
        }

        if (allProcesses) {
            this.mgr.getRunningAppProcesses();
            List<ActivityManager.RunningAppProcessInfo> l = this.mgr.getRunningAppProcesses();
            if (l != null) {
                int[] pids = new int[l.size()];
                for (int i = 0; i < l.size(); i++) {
                    pids[i] = l.get(i).pid;
                }
            }
        } else if (this.myPid != null) {
            this.mgr.getProcessMemoryInfo(this.myPid);
        }
        return (int) ((this.memInfo.availMem / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
    }

    public void onSeekComplete(MediaPlayer mp) {
        if (this.soundLog) {
            System.out.println("onSeekComplete");
        }

        if (mp == this.moviePlayer) {
            SetVideoAspect(mp);
            mp.start();
            mp.setOnSeekCompleteListener(null);
            this.bIsPlayingMovie = 2;
        }
    }

    /* access modifiers changed from: package-private */
    private void SetVideoAspect(MediaPlayer mp) {
        FrameLayout.LayoutParams layoutParams;
        if (this.customMovieSurface == null) {
            try {

                float video_Width = (float) mp.getVideoWidth();
                float video_Height = (float) mp.getVideoHeight();
                if (video_Width <= 1.0f || video_Height <= 10.0f) {
                    System.out.println("videosize error (" + video_Width + "," + video_Height + ")");
                    return;
                }
                float aspectratio = video_Width / video_Height;
            } catch (IllegalStateException e) {
                /* ~ */
            } catch (Exception e2) {
                /* ~ */
            }
        }
    }

    public void onPrepared(MediaPlayer mp) {
        if (!mp.equals(this.moviePlayer)) {
            return;
        }

        if (this.bIsPlayingMovie != 1) {
            System.out.println("trying to start a requested to stop movie");
            try {
                mp.release();
            } catch (IllegalStateException e) {
                /* ~ */
            }
            this.moviePlayer = null;
            this.bIsPlayingMovie = 0;
            ClearVidView();
            return;
        }
        this.moviePlayer.setVolume(this.currentMovieVolume, this.currentMovieVolume);
        if (this.movieLooping != 0) {
            this.moviePlayer.setLooping(true);
        }
        if (this.currentMovieMS > 0) {

            mp.seekTo(this.currentMovieMS);
        } else {
            SetVideoAspect(mp);
            try {
                mp.start();
                this.bIsPlayingMovie = 2;
            } catch (IllegalStateException e2) {
                System.out.println("onPrepared IllegalStateException " + e2.getMessage());
                this.moviePlayer = null;
                this.bIsPlayingMovie = 0;
                ClearVidView();
            }
        }
        this.currentMovieMS = 0;
    }

    public void onCompletion(MediaPlayer mp) {
        if (mp.equals(this.moviePlayer)) {
            System.out.println("onCompletion completed moviePlayer");
            this.moviePlayer = null;
            try {
                mp.release();
            } catch (IllegalStateException e) {
                /* ~ */
            }

            ClearVidView();
            if (this.bIsPlayingMovie == 2) {
                this.bIsPlayingMovie = 0;
            }
            this.lastMovieStop = System.currentTimeMillis() + 1000;
            this.MovieTextDisplayed = false;
            this.movieLooping = 0;
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        System.out.println("onError what " + what + " exra " + extra);
        return true;
    }

    public boolean IsPhone() {
        return (getResources().getConfiguration().screenLayout & 15) < 3;
    }

    public void ClearSystemNotification() {
        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass12 */
        runOnUiThread(() -> ((NotificationManager) WarMedia.this.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll());
    }

    /* access modifiers changed from: package-private */
    public void SetVidView() {
        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass13 */
        runOnUiThread(() -> {
            if (WarMedia.this.customMovieSurface != null) {
                WarMedia.this.customMovieSurface.setVisibility(View.VISIBLE);
            }
        });
    }

    public void ClearVidView() {
        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass14 */
        runOnUiThread(() -> {
            if (WarMedia.this.customMovieSurface != null) {
                WarMedia.this.customMovieSurface.setVisibility(View.GONE);
                WarMedia.this.movieView.setVisibility(View.GONE);
                WarMedia.this.customMovieSurface = null;
            }
        });
    }

    public boolean IsMovieViewActive() {

        return this.movieViewIsActive;
    }

    public void MovieSetSkippable(boolean skippable) {
        this.MovieIsSkippable = skippable;
    }

    public void StopMovie() {
        this.movieLooping = 0;
        this.MovieIsSkippable = false;
        this.MovieTextDisplayed = false;
        if (this.bIsPlayingMovie != 2) {
            if (this.DoLog) {
                System.out.println("MOVIE IS NOT PLAYING bIsPlayingMovie " + this.bIsPlayingMovie);
            }
            this.bIsPlayingMovie = 0;
        }
        try {
            if (this.moviePlayer != null) {
                this.moviePlayer.release();
                this.moviePlayer = null;
            }
        } catch (Exception e) {
            this.moviePlayer = null;
        }
        this.lastMovieStop = System.currentTimeMillis() + 1000;
        ClearVidView();
        this.bIsPlayingMovie = 0;
        this.bMoviePlayerPaused = false;
    }

    /* access modifiers changed from: package-private */
    public SurfaceHolder CreatTextSurface(SurfaceView surface) {
        SurfaceHolder holder = surface.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass15 */

            public void surfaceCreated(SurfaceHolder holder) {

                Canvas canvas = WarMedia.this.movieTextHolder.lockCanvas();
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                WarMedia.this.movieTextHolder.unlockCanvasAndPost(canvas);
                if (!WarMedia.this.movieViewCreated) {
                    System.out.println("movieTextSurface surfaceCreated firsttime");

                    return;
                }
                System.out.println("movieTextSurface surfaceCreated");
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                System.out.println("movieTextSurface surfaceChanged width " + width + " height " + height);
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                System.out.println("movieTextSurface surfaceDestroyed");

            }
        });
        System.out.println("movieWindowHolder setType");
//        holder.setType(0);
        surface.setZOrderOnTop(true);
        return holder;
    }

    /* access modifiers changed from: package-private */
    public TextView CreateTextView() {
        TextView tv = new TextView(this.activity);
        tv.setLayoutParams(new WindowManager.LayoutParams(-2, -2));
        tv.setTextSize(48.0f);
        tv.setText("Tap to Skip");
        return tv;
    }

    /* access modifiers changed from: package-private */
    public LinearLayout CreateMovieView(int x, int y, int width, int height) {
        LinearLayout ll = new LinearLayout(this.activity);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        this.row1 = new LinearLayout(this.activity);
        this.row1.setOrientation(LinearLayout.VERTICAL);
        this.row1.setLayoutParams(new LinearLayout.LayoutParams(-2, y));
        ll.addView(this.row1);
        this.row2 = new LinearLayout(this.activity);
        this.row2.setOrientation(LinearLayout.HORIZONTAL);
        this.row2.setLayoutParams(new LinearLayout.LayoutParams(-2, height));
        this.col1 = new LinearLayout(this.activity);
        this.col1.setOrientation(LinearLayout.HORIZONTAL);
        this.col1.setLayoutParams(new LinearLayout.LayoutParams(x, -2));
        this.col2 = new LinearLayout(this.activity);
        this.col2.setOrientation(LinearLayout.HORIZONTAL);
        this.col2.setLayoutParams(new LinearLayout.LayoutParams(width, -2));
        this.movieWindowSurface = new SurfaceView(this.activity);
        this.col2.addView(this.movieWindowSurface);
        this.col3 = new LinearLayout(this.activity);
        this.col3.setOrientation(LinearLayout.HORIZONTAL);
        this.col3.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        this.row2.addView(this.col1);
        this.row2.addView(this.col2);
        this.row2.addView(this.col3);
        ll.addView(this.row2);
        this.row3 = new LinearLayout(this.activity);
        this.row3.setOrientation(LinearLayout.VERTICAL);
        this.row3.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        ll.addView(this.row3);
        return ll;
    }

    /* access modifiers changed from: package-private */
    public void ChangeMovieView(int x, int y, int width, int height) {
        this.row1.setLayoutParams(new LinearLayout.LayoutParams(-2, y));
        this.row2.setLayoutParams(new LinearLayout.LayoutParams(-2, height));
        this.col1.setLayoutParams(new LinearLayout.LayoutParams(x, -2));
        this.col2.setLayoutParams(new LinearLayout.LayoutParams(width, -2));
    }

    public void PlayMovieInWindow(final String inFilename, int x, int y, int width, int height, final float inVolume, final int inOffset, final int inLength, int looping, boolean forceSize) {
        System.out.println("PlayMovieInWindow filename " + inFilename + " movieWindowSurface " + this.movieWindowSurface + " inOffset " + inOffset + " inLength " + inLength);
        this.MovieIsSkippable = false;
        this.ForceSize = forceSize;
        System.out.println("PlayMovieInWindow ForceSize " + this.ForceSize + " width " + width + " height " + height);
        if (!this.checkForMaxDisplaySize || this.baseDisplayHeight >= (height * 2) / 3) {
            this.movieViewWidth = width;
            this.movieViewHeight = height;
        } else {
            this.movieViewWidth = this.baseDisplayWidth;
            this.movieViewHeight = this.baseDisplayHeight;
        }
        this.movieViewX = x;
        this.movieViewY = y;
        this.movieLooping = looping;
        this.bIsPlayingMovie = 1;
        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass16 */
        runOnUiThread(() -> {
            movieView = CreateMovieView(movieViewX, movieViewY, movieViewWidth, movieViewHeight);
            movieWindowHolder = movieWindowSurface.getHolder();
            movieWindowHolder.addCallback(new SurfaceHolder.Callback() {
                /* class com.wardrumstudios.utils.WarMedia.AnonymousClass16.AnonymousClass1 */

                public void surfaceCreated(SurfaceHolder holder) {
                    movieViewIsActive = true;
                    if (!movieViewCreated) {
                        System.out.println("movieViewCreated surfaceCreated firsttime");
                        movieViewCreated = true;
                        return;
                    }
                    System.out.println("movieViewCreated surfaceCreated");
                }

                public void surfaceChanged(SurfaceHolder holder, int format, int width1, int height1) {
                    System.out.println("movieView surfaceChanged width " + width1 + " height " + height1);
                    movieViewWidth = width1;
                    movieViewHeight = height1;
                }

                public void surfaceDestroyed(SurfaceHolder holder) {
                    System.out.println("movieViewCreated surfaceDestroyed");
                    if (movieTextSurface != null) {
                        movieTextSurface.setVisibility(View.GONE);
                    }
                    movieViewIsActive = false;

                    movieSubtitleText = "";

                }
            });
            System.out.println("movieWindowHolder setType");
//                movieWindowHolder.setType(3);
            movieWindowSurface.setZOrderOnTop(true);
            movieWindowHolder.setFormat(-3);
            System.out.println("movieView (" + movieViewX + "," + movieViewY + ") (" + movieViewWidth + "," + movieViewHeight + ")");
            LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(-2, -2);
            myParams.gravity = 17;
            addContentView(movieView, myParams);
            movieTextSurface = new SurfaceView(activity);
            movieTextHolder = CreatTextSurface(movieTextSurface);
            movieTextHolder.setFormat(-3);
            addContentView(movieTextSurface, new WindowManager.LayoutParams(-1, -1));
            System.out.println("PlayMovieInFile for inwindow");
            customMovieSurface = movieWindowSurface;
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass16.AnonymousClass2 */
            new Thread(() -> {
                int count = 0;
                while (true) {
                    if (movieViewIsActive) {
                        break;
                    }
                    int count2 = count + 1;
                    if (count >= 10) {
                        break;
                    }
                    System.out.println("wait for create");
                    mSleep(1000);
                    count = count2;
                }
                if (movieViewIsActive) {
                    if (inLength > 0) {
                        PlayMovieInFile(inFilename, inVolume, inOffset, inLength, movieWindowHolder);
                    } else {
                        PlayMovie(inFilename, inVolume, movieWindowHolder);
                    }
                }
            }).start();
        });
    }

    public void PlayMovieInFile(String filename, float volume, int offset, int length) {

    }

    public void PlayMovieInFile(String filename, float volume, int offset, int length, SurfaceHolder myVidHolder) {
        final String tempFilename;
        String tempFilename1;
        if (!(this.customMovieSurface == null || this.customMovieSurface == this.movieWindowSurface)) {
            this.customMovieSurface = null;
        }
        this.customMovieHolder = myVidHolder;
        this.bIsPlayingMovie = 1;
        this.bMoviePlayerPaused = false;
        this.currentMovieFilename = filename;
        this.currentMovieOffset = offset;
        this.currentMovieLength = length;
        this.currentMovieVolume = volume;
        if (this.DoLog) {
            System.out.println("PlayMovieInFile " + filename + " offset " + offset + " length " + length);
        }
        if (filename.startsWith("/")) {
            tempFilename1 = Environment.getExternalStorageDirectory().getAbsolutePath() + filename;
        } else {
            tempFilename1 = this.baseDirectory + "/" + filename.replace("\\", "/");
        }
        if (!new File(tempFilename1).exists()) {
            tempFilename1 = filename;
        }
        tempFilename = tempFilename1;
        if (this.DoLog) {
            System.out.println("PlayMovieInFile " + tempFilename + " offset " + offset + " length " + length);
        }
        try {
            if (this.moviePlayer != null) {
                this.moviePlayer.release();
            }
        } catch (Exception e) {
            /* ~ */
        }
        while (this.lastMovieStop > System.currentTimeMillis()) {
            mSleep(100);
        }
        SetVidView();
        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass17 */
        runOnUiThread(() -> {
            try {
                moviePlayer = new MediaPlayer();
                moviePlayer.setDataSource(new RandomAccessFile(new File(tempFilename), "r").getFD(), (long) currentMovieOffset, (long) currentMovieLength);
                int count = 0;
                while (!IsMovieViewActive()) {
                    System.out.println("Waiting for video surface PlayMovieInFile");
                    mSleep(100);
                    count++;
                    if (count > 5) {
                        break;
                    }
                }
                if (count <= 5) {
                    moviePlayer.setDisplay(customMovieHolder);

                    moviePlayer.prepareAsync();
                    return;
                }
                System.out.println("creation failed count " + count);
                ClearVidView();
                moviePlayer.release();
                moviePlayer = null;
                bIsPlayingMovie = 0;
            } catch (Exception ex) {
                System.out.println("creation failed error  " + ex.getMessage());
                moviePlayer = null;
                bIsPlayingMovie = 0;
            }
        });
    }

    public void PlayMovie(String filename, float Volume) {

    }

    public void PlayMovie(String filename, float Volume, SurfaceHolder myVidHolder) {
        String apkFilename;
        this.customMovieHolder = myVidHolder;
        this.bIsPlayingMovie = 1;
        this.currentMovieFilename = filename;
        this.currentMovieOffset = 0;
        this.currentMovieLength = 0;
        this.currentMovieVolume = Volume;
        if (this.DoLog) {
            System.out.println("PlayMovie " + filename);
        }
        final String newFile = this.baseDirectory + "/" + filename.replace("\\", "/") + (this.AddMovieExtension ? ".m4v" : "");
        if (this.DoLog) {
            System.out.println("PlayMovie newFile " + newFile);
        }
        AssetFileDescriptor afd = null;
        if (!new File(newFile).exists()) {
            try {
                if (this.AddMovieExtension) {
                    apkFilename = filename.replace("\\", "/") + ".m4v.mp3";
                } else {
                    apkFilename = filename.replace("\\", "/") + ".mp3";
                }
                System.out.println("Looking for  " + apkFilename);
                afd = getApplicationContext().getAssets().openFd(apkFilename);
            } catch (Exception e) {
                afd = null;
            }
            if (afd == null) {
                System.out.println("File not found " + filename);
                this.bIsPlayingMovie = 0;
                return;
            }
        }
        try {
            if (this.moviePlayer != null) {
                this.moviePlayer.release();
            }
        } catch (Exception e2) {
            /* ~ */
        }
        while (this.lastMovieStop > System.currentTimeMillis()) {
            mSleep(100);
        }
        SetVidView();
        AssetFileDescriptor finalAfd = afd;
        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass18 */
        runOnUiThread(() -> {
            try {
                moviePlayer = new MediaPlayer();
                if (finalAfd != null) {
                    moviePlayer.setDataSource(finalAfd.getFileDescriptor(), finalAfd.getStartOffset(), finalAfd.getLength());
                } else {
                    moviePlayer.setDataSource(newFile);
                }
                int count = 0;
                while (!IsMovieViewActive()) {
                    System.out.println("Waiting for video surface PlayMovie");
                    count++;
                    if (count > 5) {
                        try {
                            moviePlayer.release();
                        } catch (IllegalStateException e) {
                            /* ~ */
                        }
                        moviePlayer = null;
                        ClearVidView();
                    }
                    mSleep(100);
                }
                moviePlayer.setDisplay(customMovieHolder);

                moviePlayer.prepareAsync();
            } catch (Exception e2) {
                /* ~ */
            }
        });
    }

    public int IsMoviePlaying() {
        if (this.bMoviePlayerPaused) {
            return 2;
        }
        if (this.bIsPlayingMovie == 1) {
            return this.bIsPlayingMovie;
        }
        if (this.bIsPlayingMovie != 2 || this.moviePlayer == null) {
            return 0;
        }
        try {
            if (this.moviePlayer.isPlaying()) {
                return this.bIsPlayingMovie;
            }
            return 0;
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    public String GetConfigSetting(String key) {
        String value = getPreferences(0).getString(key, "");
        if (this.DoLog) {
            System.out.println("GetConfigSetting " + key + " value " + value);
        }
        return value;
    }

    public void SetConfigSetting(String key, String value) {
        SharedPreferences.Editor e = getPreferences(0).edit();
        e.putString(key, value);
        e.apply();
        if (this.DoLog) {
            System.out.println("SetConfigSetting " + key + " value " + value);
        }
    }

    public String OBFU_GetDeviceID() {
        System.out.println("**** OBFU_GetDeviceID");
        return "no id";
    }

    public void GetRealLocale() {
        Locale langLocal = Locale.getDefault();
        String lang = Locale.getDefault().getLanguage();
        String locale2 = Locale.getDefault().getDisplayLanguage();
        this.DeviceCountry = Locale.getDefault().getCountry();
        if (this.DoLog) {
            System.out.println("SetLocale getDefault " + lang + " langLocal " + langLocal + " locale " + locale2 + " DeviceCountry " + this.DeviceCountry);
        }
        this.DeviceLocale = lang;
    }

    public int GetDeviceLocale() {
        String lang = this.DeviceLocale;
        if (lang.equals("en")) {
            return 0;
        }
        if (lang.equals("fr")) {
            return 1;
        }
        if (lang.equals("de")) {
            return 2;
        }
        if (lang.equals("it")) {
            return 3;
        }
        if (lang.equals("es")) {
            return 4;
        }
        if (lang.equals("ja")) {
            return 5;
        }
        if (lang.equals("ko")) {
            return 6;
        }
        if (lang.equals("sv")) {
            return 7;
        }
        if (lang.equals("no") || lang.equals("nb") || lang.equals("nn")) {
            return 8;
        }
        if (lang.equals("ru")) {
            return 9;
        }
        return 0;
    }

    public int GetLocale() {
        String lang = GetConfigSetting("currentLanguage");
        if (lang.equals("en")) {
            return 0;
        }
        if (lang.equals("fr")) {
            return 1;
        }
        if (lang.equals("de")) {
            return 2;
        }
        if (lang.equals("it")) {
            return 3;
        }
        if (lang.equals("es")) {
            return 4;
        }
        if (lang.equals("ja")) {
            return 5;
        }
        if (lang.equals("ko")) {
            return 6;
        }
        if (lang.equals("sv")) {
            return 7;
        }
        if (lang.equals("no") || lang.equals("nb") || lang.equals("nn")) {
            return 8;
        }
        return 0;
    }

    public void SetLocale(String lStr) {
        GetRealLocale();
        if (this.DoLog) {
            System.out.println("SetLocale " + lStr);
        }
        String lang = GetConfigSetting("currentLanguage");
        if (this.DoLog) {
            System.out.println("SetLocale oldlang " + lang);
        }
        String countyStr = "";
        if (lStr.equals("en")) {
            if (this.DeviceCountry.equals("GB")) {
                countyStr = "GB";
            } else {
                countyStr = "US";
            }
        }
        this.locale = new Locale(lStr, countyStr);
        Locale.setDefault(this.locale);
        SetConfigSetting("currentLanguage", lStr);
    }

    public void RestoreCurrentLanguage() {
        String lang = GetConfigSetting("currentLanguage");
        String countyStr = "";
        if (!lang.equals("")) {
            if (lang.equals("en")) {
                if (this.DeviceCountry.equals("GB")) {
                    countyStr = "GB";
                } else {
                    countyStr = "US";
                }
            }
            this.locale = new Locale(lang, countyStr);
            Locale.setDefault(this.locale);
        }
    }

    public void SetLocale(int newLang) {
        String lStr;
        switch (newLang) {
            case 1:
                lStr = "fr";
                break;
            case 2:
                lStr = "de";
                break;
            case 3:
                lStr = "it";
                break;
            case 4:
                lStr = "es";
                break;
            case 5:
                lStr = "ja";
                break;
            case 6:
                lStr = "ko";
                break;
            case 7:
                lStr = "sv";
                break;
            case 8:
                lStr = "no";
                break;
            default:
                lStr = "en";
                break;
        }
        if (this.DoLog) {
            System.out.println("SetLocale " + newLang + " lStr " + lStr);
        }
        String lang = GetConfigSetting("currentLanguage");
        if (this.DoLog) {
            System.out.println("SetLocale oldlang " + lang);
        }
        this.locale = new Locale(lStr);
        Locale.setDefault(this.locale);
        SetConfigSetting("currentLanguage", lStr);
    }

    public boolean DeleteFile(String filename) {
        String fullFilename = this.baseDirectory + "/" + filename.replace('\\', '/');
        File delfile = new File(fullFilename);
        if (this.DoLog) {
            System.out.println("trying to delete file " + fullFilename);
        }
        if (!delfile.exists()) {
            return false;
        }
        if (this.DoLog) {
            System.out.println("Deleting file " + fullFilename);
        }
        return delfile.delete();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean FileRename(String oldfile, String newfile, int overWrite) {
        new File(this.baseDirectory + "/" + oldfile.replace('\\', '/')).renameTo(new File(this.baseDirectory + "/" + newfile.replace('\\', '/')));
        return true;
    }

    public String FileGetArchiveName(int i) {
        System.out.println("**** FileGetArchiveName");
        switch (i) {
            case 0:
                return apkFileName;
            case 1:
                return expansionFileName;
            case 2:
                return patchFileName;
            default:
                return "";
        }
    }

    public boolean IsTV() {
        return this.IsAndroidTV;
    }

    public String GetAndroidBuildinfo(int index) {
        switch (index) {
            case 0:
                return Build.MANUFACTURER;
            case 1:
                return Build.PRODUCT;
            case 2:
                return Build.MODEL;
            case 3:
                return Build.HARDWARE;
            default:
                return "UNKNOWN";
        }
    }

    public int GetDeviceInfo(int index) {
        switch (index) {
            case 0:
                return getNumberOfProcessors();
            case 1:
                return this.hasTouchScreen ? 1 : 0;
            default:
                return -1;
        }
    }

    public int GetDeviceType()
    {
        int i = 0;
        System.out.println("Build info version device  " + Build.DEVICE);
        System.out.println("Build MANUFACTURER  " + Build.MANUFACTURER);
        System.out.println("Build BOARD  " + Build.BOARD);
        System.out.println("Build DISPLAY  " + Build.DISPLAY);
        System.out.println("Build CPU_ABI  " + Build.CPU_ABI);
        System.out.println("Build CPU_ABI2  " + Build.CPU_ABI2);
        System.out.println("Build HARDWARE  " + Build.HARDWARE);
        System.out.println("Build MODEL  " + Build.MODEL);
        System.out.println("Build PRODUCT  " + Build.PRODUCT);
        int i2 = 0;
        int numberOfProcessors = 1 * 4;
        int i3 = 8 * 64;
        if (IsPhone())
        {
            i = 1;
        }
        return i + i2 + numberOfProcessors + i3;
    }

    private int getNumberOfProcessors() {
        try {
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass1CpuFilter */
            File file = new File("/sys/devices/system/cpu/");
            return file.listFiles(pathname -> Pattern.matches("cpu[0-9]", pathname.getName())).length;
        } catch (Exception e) {
            return 1;
        }
    }

    public void ShowKeyboard(int show) {
        if (getResources().getConfiguration().hardKeyboardHidden != 1) {
            InputMethodManager myImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (show != 0) {
                myImm.toggleSoftInput(2, 0);
                return;
            }
            View tbview = getCurrentFocus();
            if (tbview != null) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(tbview.getWindowToken(), 0);
            }
            myImm.toggleSoftInput(0, 0);
            System.out.println("hideSystemUI");
            hideSystemUI();
        }
    }

    public boolean IsKeyboardShown() {
        return false;
    }

    private void setState(int newState) {
        if (this.mState != newState) {
            this.mState = newState;
            this.mStatusText.setText(Helpers.getDownloaderStringResourceIDFromState(getApplicationContext(), newState));
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setButtonPausedState(boolean paused) {
        int stringResourceID;
        this.mStatePaused = paused;
        if (paused) {
            stringResourceID = Helpers.GetResourceIdentifier(getApplicationContext(), "text_button_resume", "string");
        } else {
            stringResourceID = Helpers.GetResourceIdentifier(getApplicationContext(), "text_button_pause", "string");
        }
        this.mPauseButton.setText(stringResourceID);
    }


    public String GetPackageName(String str) {
        List<ApplicationInfo> installedApplications = getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (int i = 0; i < installedApplications.size(); i++) {
            if (str.compareToIgnoreCase(installedApplications.get(i).packageName.toString()) == 0) {
                return installedApplications.get(i).sourceDir;
            }
        }
        return "";
    }

    public boolean IsAppInstalled(String str) {
        List<ApplicationInfo> installedApplications = getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (int i = 0; i < installedApplications.size(); i++) {
            System.out.println("app[" + i + "]=" + installedApplications.get(i).packageName + " dir " + installedApplications.get(i).sourceDir);
            if (str.compareToIgnoreCase(installedApplications.get(i).packageName.toString()) == 0) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void OpenLink(String link) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(link)));
    }

    public boolean IsCloudAvailable() {
        return false;
    }

    public void LoadAllGamesFromCloud() {
    }

    public String LoadGameFromCloud(int slot, byte[] array) {
        return "";
    }

    public void SaveGameToCloud(int slot, byte[] array, int numbytes) {
    }

    public boolean NewCloudSaveAvailable(int slot) {
        return false;
    }

    public void MovieKeepAspectRatio(boolean keep) {
        this.KeepAspectRatio = keep;
    }

    /* access modifiers changed from: package-private */
    public void ClearMovieText() {
        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass20 */
        runOnUiThread(() -> {
            Canvas canvas = movieTextHolder.lockCanvas();
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            movieTextHolder.unlockCanvasAndPost(canvas);
        });
    }

    /* access modifiers changed from: package-private */
    private void MovieSetTextScale(int scale) {
        this.movieTextScale = scale;
        SetPaint(-16711936, this.movieTextScale);
    }

    /* access modifiers changed from: package-private */
    private void SetPaint(int color, int size) {
        this.mTextPaint = new Paint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize((float) size);
        this.mTextPaint.setColor(color);
        this.mAscent = (int) this.mTextPaint.ascent();
        this.tPaint = new Paint();
        this.textPaint = new TextPaint();
        this.textPaint.setColor(-1);
        this.textPaint.setTextSize((float) this.movieTextScale);
        this.sTextPaint = new TextPaint();
        this.sTextPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.sTextPaint.setTextSize((float) this.movieTextScale);
    }

    /* access modifiers changed from: package-private */
    private void DrawMovieText() {
        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass21 */
        runOnUiThread(() -> {
            boolean clearText = true;
            if ( movieSubtitleText.length() > 0) {
                clearText = false;
                movieTextSurface.setVisibility(View.VISIBLE);
                String drawText = "";
                Canvas canvas = movieTextHolder.lockCanvas();
                if (canvas != null) {
                    StaticLayout layoutText = new StaticLayout(drawText, textPaint, surfaceWidth - 100, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);
                    StaticLayout sLayoutText = new StaticLayout(drawText, sTextPaint, surfaceWidth - 100, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    canvas.translate(52.0f, (float) ((surfaceHeight - sLayoutText.getHeight()) - 5));
                    sLayoutText.draw(canvas);
                    canvas.translate(-2.0f, -2.0f);
                    layoutText.draw(canvas);
                    movieTextHolder.unlockCanvasAndPost(canvas);
                }
            }

            if (clearText) {
                Canvas canvas2 = movieTextHolder.lockCanvas();
                if (canvas2 != null) {
                    canvas2.drawColor(0, PorterDuff.Mode.CLEAR);
                    movieTextHolder.unlockCanvasAndPost(canvas2);
                }
                movieTextSurface.setVisibility(View.GONE);
            }
        });
    }

    /* access modifiers changed from: package-private */
    private void DisplayMovieText() {
        System.out.println("DisplayMovieText vidViewIsActive ");
        DrawMovieText();
    }

    public void MovieClearText(boolean isSubtitle) {
        if (isSubtitle) {
            this.movieSubtitleText = "";
        } else {

        }

        DrawMovieText();
    }

    public void MovieSetText(String text, boolean DisplayNow, boolean isSubtitle) {
        if (isSubtitle) {
            this.movieSubtitleText = text;
        } else {
            this.DisplayMovieTextOnTap = !DisplayNow;
            this.movieTextDisplayNow = DisplayNow;
        }

        if (DisplayNow) {
            DisplayMovieText();
        } else {
            /* class com.wardrumstudios.utils.WarMedia.AnonymousClass22 */
            runOnUiThread(() -> {
                System.out.println("MovieSetText create surface");
                movieTextSurface.setVisibility(View.VISIBLE);
            });
        }
    }

    public void MovieDisplayText(boolean display) {
        System.out.println("MovieDisplayText " + display);
        if (display && !this.MovieTextDisplayed) {
            DisplayMovieText();
        }
    }

    public void DisplaySplashScreen(final String splashPng, final int splashTimer) {
        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass23 */
        runOnUiThread(() -> {
            try {
                /* class com.wardrumstudios.utils.WarMedia.AnonymousClass23.AnonymousClass1 */
                handler.postDelayed(WarMedia.this::ClearSplashScreen, (long) splashTimer);
                ImageView splashView = new ImageView(getApplicationContext());
                llSplashView = new LinearLayout(getApplicationContext());
                Drawable dr = Drawable.createFromStream(getAssets().open(splashPng), null);
                int width = dr.getIntrinsicWidth() - 40;
                int height = dr.getIntrinsicHeight() - 40;
                splashView.setImageDrawable(dr);
                int surfaceView_Width = baseDisplayWidth;
                int surfaceView_Height = baseDisplayHeight;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                float aspectratio = ((float) width) / ((float) height);
                if (((float) surfaceView_Width) / ((float) width) > ((float) surfaceView_Height) / ((float) height)) {
                    layoutParams.width = (int) (((float) surfaceView_Height) * aspectratio);
                    layoutParams.height = surfaceView_Height;
                } else {
                    layoutParams.width = surfaceView_Width;
                    layoutParams.height = (int) (((float) surfaceView_Width) / aspectratio);
                }

                int i = (surfaceView_Width - layoutParams.width) / 2;
                int i2 = (surfaceView_Height - layoutParams.height) / 2;
                layoutParams.gravity = 17;
                llSplashView.addView(splashView, layoutParams);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -1);
                layoutParams2.gravity = 17;
                addContentView(llSplashView, layoutParams2);
            } catch (Exception e) {
                llSplashView = null;
                System.out.println("DisplaySplashScreeen Error");
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void ClearSplashScreen() {
        /* class com.wardrumstudios.utils.WarMedia.AnonymousClass24 */
        runOnUiThread(() -> {
            if (DoLog) {
                System.out.println("Clearing SplashScreen ");
            }

            if (llSplashView != null) {
                llSplashView.setVisibility(View.GONE);
            }
            llSplashView = null;
        });
    }

    public int GetSpecialBuildType() {
        return SpecialBuildType;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void DeleteDirectory(String dirString, boolean recurse) {
        File dir = new File(dirString);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String s : children) {
                    File child = new File(dir, s);
                    if (child.isDirectory()) {
                        DeleteDirectory(dir + "/" + s, recurse);
                    }

                    System.out.println("delete " + dir + "/" + s);
                    child.delete();
                }
            }

            dir.delete();
        }
    }

    public boolean CustomLoadFunction() {
        return false;
    }

    public void AfterDownloadFunction() {
        DoResumeEvent();
    }

    public void SendStatEvent(String eventId, boolean timedEvent) {
        /* ~ */
    }

    public void SendTimedStatEventEnd(String eventId) {
        /* ~ */
    }

    public void SendStatEvent(String eventId, String paramName, String paramValue, boolean timedEvent) {
        /* ~ */
    }

    public int GetTotalMemory() {
        return totalMemory;
    }

    public float GetScreenWidthInches() {
        return screenWidthInches;
    }

    public int GetLowThreshhold() {
        return memoryThreshold;
    }

    public int GetAvailableMemory() {
        try {
            Runtime info = Runtime.getRuntime();
            long usedSize = info.totalMemory() - ((long) ((int) ((info.freeMemory() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        GetMemoryInfo(false);
        return availableMemory;
    }

    public String GetAppId() {
        return getPackageName();
    }

    public void ScreenSetWakeLock(boolean enable) {
        if (enable) {
            myWakeLock.acquire(10*60*1000L /*10 minutes*/);
        } else {
            myWakeLock.release();
        }
    }

    public void CreateTextBox(int id, int x, int y, int x2, int y2) {
        /* ~ */
    }

    public boolean CopyFileFromAssets(String filename, String destPathName, boolean overwrite) {
        File ringtone = new File(destPathName);
        if (ringtone.exists()) {
            return false;
        }

        if (ringtone.getParentFile() != null) {
            ringtone.getParentFile().mkdirs();
        }

        try {
            InputStream in = getAssets().open(filename);
            OutputStream out = new FileOutputStream(destPathName);
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int read = in.read(buffer);
                    if (read != -1) {
                        out.write(buffer, 0, read);
                    } else {
                        in.close();
                        out.flush();
                        out.close();
                        return true;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    public boolean ConvertToBitmap(byte[] data, int length) {
        return false;
    }

    public boolean ServiceAppCommand(String cmd, String args) {
        return false;
    }

    public int ServiceAppCommandValue(String cmd, String args) {
        return 0;
    }

    public boolean ServiceAppCommandInt(String cmd, int args) {
        return false;
    }
}
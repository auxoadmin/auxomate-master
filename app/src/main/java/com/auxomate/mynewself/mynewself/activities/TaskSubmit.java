package com.auxomate.mynewself.mynewself.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.auxomate.mynewself.mynewself.R;
import com.auxomate.mynewself.mynewself.utilities.AlarmReceiver;
import com.auxomate.mynewself.mynewself.utilities.NotificationScheduler;
import com.auxomate.mynewself.mynewself.utilities.PrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskSubmit extends AppCompatActivity implements View.OnClickListener{
    private String result = null;
    private String resultArray[] = null;

    private EditText editTextPTOne, editTextSTTwo, editTextSTThree, editTextSchTOneTime, editTextSchTOneDuration,
    editTextSchTTwoTime, editTextSchTTwoDuration, editTextSchTThreeTime, editTextSchTThreeDuration, editTextSchVOneTime,
    editTextSchVTwoTime, editTextSchVThreeTime;

    private Button buttonSubmit;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;

    String TAG = "RemindMe";
    PrefManager localData;
    AspireGallery aspireGallery;

    private static int tOnehour, tOnemin,tTwohour,tTwomin,tThreehour,tThreemin,vOnehour,vOnemin,vTwohour,vTwomin,vThreehour,vThreemin;

    ClipboardManager myClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_submit);

        result = getIntent().getStringExtra("visionResult");
        localData = new PrefManager(getApplicationContext());
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        aspireGallery = new AspireGallery();
        init();
    }

    private void init() {

        editTextPTOne = findViewById(R.id.tasksubmit_edittext_pttaskone);
        editTextSTTwo = findViewById(R.id.tasksubmit_edittext_sttasktwo);
        editTextSTThree = findViewById(R.id.tasksubmit_edittext_sttaskthree);
        editTextSchTOneTime = findViewById(R.id.tasksubmit_edittext_schttaskonetime);
        editTextSchTOneDuration = findViewById(R.id.tasksubmit_edittext_schttaskoneduration);
        editTextSchTTwoTime = findViewById(R.id.tasksubmit_edittext_schttasktwotime);
        editTextSchTTwoDuration = findViewById(R.id.tasksubmit_edittext_schttasktwoduration);
        editTextSchTThreeTime = findViewById(R.id.tasksubmit_edittext_schttaskthreetime);
        editTextSchTThreeDuration = findViewById(R.id.tasksubmit_edittext_schttaskthreeduration);
        editTextSchVOneTime = findViewById(R.id.tasksubmit_edittext_schvonetime);
        editTextSchVTwoTime = findViewById(R.id.tasksubmit_edittext_schvtwotime);
        editTextSchVThreeTime = findViewById(R.id.tasksubmit_edittext_schvthreetime);

        buttonSubmit = findViewById(R.id.tasksubmit_button_submit);
        buttonSubmit.setOnClickListener(this);
        editTextSchTOneTime.setOnClickListener(this);
        editTextSchTTwoTime.setOnClickListener(this);
        editTextSchTThreeTime.setOnClickListener(this);
        editTextSchVOneTime.setOnClickListener(this);
        editTextSchVTwoTime.setOnClickListener(this);
        editTextSchVThreeTime.setOnClickListener(this);

        getIncomingIntent();

    }

    private void getIncomingIntent(){

        if(getIntent().hasExtra("visionResult")) {

            String[] split = result.split("(\\s|^)Primary Task(\\s|$)");
            if (2 <= split.length) {
                String dailyQuote = split[0].toString();
                String stringTask1 = split[1].toString();
                String[] splitTask = stringTask1.split("(\\s|^)Secondary Tasks[\\r\\n]Task 2(\\s|$)|(\\s|^)SecondaryTasks[\\r\\n]Task2(\\s|$)");
                String[] splitTask1 = splitTask[0].split("(\\s|^)Task 1(\\s|$)|(\\s|^)Task1(\\s|$)|(\\s|^)Taski(\\s|$)|(\\s|^)Task i(\\s|$)|(\\s|^)Taskı(\\s|$)|(\\s|^)Task(\\s|$)");
                if ( 2 <= splitTask1.length) {
                    editTextPTOne.setText(splitTask1[1]);

                if (2 <= splitTask.length) {
                String[] splitTask2 = splitTask[1].split("(\\s|^)Task 3(\\s|$)|(\\s|^)Task3(\\s|$)");
                if(2<=splitTask2.length) {
                    editTextSTTwo.setText(splitTask2[0]);
                    String[] splitTask3 = splitTask2[1].split("(\\s|^)Schedule(\\s|$)");
                    if (2 <= splitTask3.length) {
                        editTextSTThree.setText(splitTask3[0]);



                        }
                    else {
                        showDialog(this,"oops","Something went wrong!!");
                    }
                    }
                else {
                    showDialog(this,"oops","Something went wrong!!");
                }
                }
                else {
                    showDialog(this,"oops","Something went wrong!!");
                }
            }
                else {
                    showDialog(this,"oops","Something went wrong!!");
                }
        }
            else {
                showDialog(this,"oops","Something went wrong!!");
            }

            String[] splitSchedule = result.split("(\\s|^)Schedule Tasks(\\s|$)");

            if(2<=splitSchedule.length) {
                String[] splitVisual = splitSchedule[1].split("(\\s|^)Schedule Visualizations(\\s|$)");
                Log.d("taskTime", splitVisual[0]);
                Log.d("VisulizationTime", splitVisual[1]);


                String[] splitvTask3 = splitVisual[0].split("(\\s|^)Task 3(\\s|$)|(\\s|^)Task3(\\s|$)");
                String[] splitvTask2 = splitvTask3[0].split("(\\s|^)Task2(\\s|$)|(\\s|^)Task 2(\\s|$)");
                String[] vtask = splitvTask2[0].split("(\\s|^)Task 1(\\s|$)|(\\s|^)Task1(\\s|$)|(\\s|^)Taski(\\s|$)|(\\s|^)Task i(\\s|$)|(\\s|^)Taskı(\\s|$)");

                if(2<=vtask.length) {
                    String schttask1 = vtask[1].split("/")[0];
                    String[] taskOneTime=schttask1.split(":");
                    NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, Integer.parseInt(taskOneTime[0]), Integer.parseInt(taskOneTime[1]),1);
                    editTextSchTOneTime.setText(schttask1);
                    if (2 <= splitvTask2.length) {
                        String schttask2 = splitvTask2[1].split("/")[0];
                        String[] taskTwoTime=schttask2.split(":");
                        editTextSchTTwoTime.setText(schttask2);
                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, Integer.parseInt(taskTwoTime[0]), Integer.parseInt(taskTwoTime[1]),2);
                        if (2 <= splitvTask3.length) {
                            String schttask3 = splitvTask3[1].split("/")[0];
                            String[] taskThreeTime=schttask3.split(":");
                            Log.d("taskthreehour",taskThreeTime[0]);
                            Log.d("taskthreemin",taskThreeTime[1]);
                            NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, Integer.parseInt(taskThreeTime[0]), Integer.parseInt(taskThreeTime[1]),3);
                            editTextSchTThreeTime.setText(schttask3);


                            String[] visualTime3 = splitVisual[1].split("(\\s|^)v 3(\\s|$)|(\\s|^)v3(\\s|$)|(\\s|^)V3(\\s|$)|(\\s|^)03(\\s|$)");
                            String[] visualTime2 = visualTime3[0].split("(\\s|^)v 2(\\s|$)|(\\s|^)v2(\\s|$)|(\\s|^)V2(\\s|$)");
                            String[] visualTime1 = visualTime2[0].split("(\\s|^)v 1(\\s|$)|(\\s|^)v1(\\s|$)|(\\s|^)V1(\\s|$)|(\\s|^)vi(\\s|$)");

                            if ( 2 <= visualTime1.length) {
                                editTextSchVOneTime.setText(visualTime1[1]);
                                String[] vOneTime = visualTime1[1].split(":");
                                NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, Integer.parseInt(vOneTime[0]), Integer.parseInt(vOneTime[1]),4);

                                if (2 <= visualTime2.length) {
                                    editTextSchVTwoTime.setText(visualTime2[1]);
                                    String[] vTwoTime = visualTime2[1].split(":");
                                    NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, Integer.parseInt(vTwoTime[0]), Integer.parseInt(vTwoTime[1]),5);

                                    if (2 <= visualTime3.length) {
                                        editTextSchVThreeTime.setText(visualTime3[1]);
                                        String[] vThreeTime = visualTime3[1].split(":");
                                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, Integer.parseInt(vThreeTime[0]), Integer.parseInt(vThreeTime[1]),6);
                                    }
                                }
                            }

                        }
                    }
                }
            }

        }
    }
    public void showDialog(Activity activity, String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });
        builder.setNegativeButton("edit menually", null);
        builder.show();
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tasksubmit_button_submit:



                submitTheTask();

                break;

            case R.id.tasksubmit_edittext_schttaskonetime:
                Log.d("edittext one","clicked");
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TaskSubmit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        localData.set_tonehour(hourOfDay);
                        localData.set_tonemin(minutes);


                        //scheduleNotification(getNotification("Task 1"),hourOfDay,minutes,1);
                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_tonehour(), localData.get_tonemin(),1);

                        Log.d("hour","value:"+hourOfDay);
                        editTextSchTOneTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.tasksubmit_edittext_schttasktwotime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TaskSubmit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        localData.set_ttwohour(hourOfDay);
                        localData.set_ttwomin(minutes);


                        //scheduleNotification(getNotification("Task 2"),hourOfDay,minutes,2);
                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_ttwohour(), localData.get_ttwomin(),2);

                        editTextSchTTwoTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.tasksubmit_edittext_schttaskthreetime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TaskSubmit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        localData.set_tthreehour(hourOfDay);
                        localData.set_tthreemin(minutes);

                        //scheduleNotification(getNotification("Task 3"),hourOfDay,minutes,3);
                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_tthreehour(), localData.get_tthreemin(),3);
                        Log.d("getHourFromPref",localData.get_tonehour()+":"+localData.get_vonemin());


                        editTextSchTThreeTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.tasksubmit_edittext_schvonetime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TaskSubmit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        localData.set_vonehour(hourOfDay);
                        localData.set_vonemin(minutes);

                        //scheduleNotification(getNotification("Visulization 1"),hourOfDay,minutes,4);
                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_vonehour(), localData.get_vonemin(),4);

                        editTextSchVOneTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.tasksubmit_edittext_schvtwotime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TaskSubmit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        localData.set_vtwohour(hourOfDay);
                        localData.set_vtwoemin(minutes);
                       // scheduleNotification(getNotification("Visulization 2"),hourOfDay,minutes,5);

                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_vtwohour(), localData.get_vtwomin(),5);

                        editTextSchVTwoTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
            case R.id.tasksubmit_edittext_schvthreetime:
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(TaskSubmit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }

                        localData.set_vthreehour(hourOfDay);
                        localData.set_vthreemin(minutes);

                        //scheduleNotification(getNotification("Visulization 3"),hourOfDay,minutes,6);

                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_vthreehour(), localData.get_vthreemin(),6);

                        editTextSchVThreeTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);

                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;




        }



    }

    private void submitTheTask() {
        PrefManager prefManager;

        String taskOneDesc = editTextPTOne.getText().toString().trim();
        String taskTwoDesc = editTextSTTwo.getText().toString().trim();
        String taskThreeDesc = editTextSTThree.getText().toString().trim();

        String scheduleTaskOneTime = editTextSchTOneTime.getText().toString().trim();
        String scheduleTaskOneDuration = editTextSchTOneDuration.getText().toString().trim();
        String scheduleTaskTwoTime = editTextSchTTwoTime.getText().toString().trim();
        String scheduleTaskTwoDuration = editTextSchTTwoDuration.getText().toString().trim();
        String scheduleTaskThreeTime = editTextSchTThreeTime.getText().toString().trim();
        String scheduleTaskThreeDuration = editTextSchTThreeDuration.getText().toString().trim();

        String scheduleVOneTime = editTextSchVOneTime.getText().toString().trim();
        String scheduleVTwoTime = editTextSchVTwoTime.getText().toString().trim();
        String scheduleVThreeTime = editTextSchVThreeTime.getText().toString().trim();

        PrefManager.putString(this,PrefManager.TASK1_DES,taskOneDesc);
        PrefManager.putString(this,PrefManager.TASK2_DES,taskTwoDesc);
        PrefManager.putString(this,PrefManager.TASK3_DES,taskThreeDesc);


        PrefManager.putString(this,PrefManager.TASK1Time,scheduleTaskOneTime);
        PrefManager.putString(this,PrefManager.TASK2Time,scheduleTaskTwoTime);
        PrefManager.putString(this,PrefManager.TASK3Time,scheduleTaskThreeTime);
        PrefManager.putString(this,PrefManager.V1Time,scheduleVOneTime);
        PrefManager.putString(this,PrefManager.V2Time,scheduleVTwoTime);
        PrefManager.putString(this,PrefManager.V3Time,scheduleVThreeTime);

        Intent i = new Intent(this,HomeActivity.class);
       // i.putExtra("quote",taskOneDesc);
        startActivity(i);





    }


//    private void showTimePickerDialog(int h, int m) {
//
//        LayoutInflater inflater = getLayoutInflater();
//
//
//        TimePickerDialog builder = new TimePickerDialog(this, R.style.DialogTheme,
//                new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
//                        Log.d(TAG, "onTimeSet: hour " + hour);
//                        Log.d(TAG, "onTimeSet: min " + min);
//                        localData.set_hour(hour);
//                        localData.set_min(min);
//                        tvTime.setText(getFormatedTime(hour, min));
//                        NotificationScheduler.setReminder(TaskSubmit.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());
//
//
//                    }
//                }, h, m, false);
//
//        builder.setCustomTitle(view);
//        builder.show();
//
//    }
//
    public String getFormatedTime(int h, int m) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";

        String oldDateString = h + ":" + m;
        String newDateString = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;
    }


//    private void scheduleNotification(Notification notification, int hour,int min,int reqCode) {
//
//        Calendar calendar = Calendar.getInstance();
//
//        Calendar setCalendar = Calendar.getInstance();
//        setCalendar.set(Calendar.HOUR_OF_DAY, hour);
//        setCalendar.set(Calendar.MINUTE, min);
//        setCalendar.set(Calendar.SECOND, 0);
//
//        // cancel already scheduled reminders
//        //cancelReminder(context,cls);
//
//        if(setCalendar.before(calendar))
//            setCalendar.add(Calendar.DATE,1);
//
//        // Enable a receiver
//
//        ComponentName receiver = new ComponentName(this, AlarmReceiver.class);
//        PackageManager pm = this.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
//
//        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
//        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1);
//        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        am.set(AlarmManager.ELAPSED_REALTIME, setCalendar.getTimeInMillis(), pendingIntent);
//
//
//    }
//
//    private Notification getNotification(String content) {
//
////        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
////
////        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
////
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
////
////            // Configure the notification channel.
////            notificationChannel.setDescription("Channel description");
////            notificationChannel.enableLights(true);
////            notificationChannel.setLightColor(Color.RED);
////            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
////            notificationChannel.enableVibration(true);
////            notificationManager.createNotificationChannel(notificationChannel);
////
////
////
////        }
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentTitle("Scheduled Notification");
//        builder.setContentText(content);
//        builder.setSmallIcon(R.drawable.logo_small);
//        return builder.build();
//
//    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }
}

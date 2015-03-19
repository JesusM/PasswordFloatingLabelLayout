FloatingLabelLayout with custom behaviours
===========================
Forked from FloatingLabelLayout https://gist.github.com/chrisbanes/11247418, the lib provides floating text label with extra behaviors based on customizable criteria.

![Demo gif](https://github.com/JesusM/PasswordFloatingLabelLayout/blob/master/images/FloatLabelLayoutGif.gif?raw=true)


[Video] (http://youtu.be/_oNO-fIk1Ys)

You need to add this to your xml layout:

````XML
<jesusm.floatlabellayout.lib.PasswordFloatLabelLayoutCheck
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:floatLabelTextAppearance="@style/TextAppearance.YourApp.FloatLabel"
        app:showStateIcon="true">
        
        <!-- Your EditText -->
        
</jesusm.floatlabellayout.lib.PasswordFloatLabelLayoutCheck>
````

app:showStateIcon (false by default) indicates if you want to show an icon when your password is considered as "strong" (you can change this behavior).

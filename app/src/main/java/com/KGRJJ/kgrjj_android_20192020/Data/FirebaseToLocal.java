package com.KGRJJ.kgrjj_android_20192020.Data;

        import android.database.sqlite.SQLiteOpenHelper;

        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseToLocal{
    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirebaseToLocal(FirebaseUser user){

    }
}

package com.example.myaip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Student> students;
    private RecyclerView rclStudent;
    private StudentAdapter studentAdapter;
    private Button btnAdd;
    private final String url="https://5fd073461f23740016631a1d.mockapi.io/myAPI/Student";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        students = new ArrayList<>();
        rclStudent =findViewById(R.id.rclStudenName);
        btnAdd = findViewById(R.id.btnAdd);

        studentAdapter = new StudentAdapter(MainActivity.this,students,url);
        rclStudent.setAdapter(studentAdapter);
        rclStudent.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        initData();

        btnAdd.setOnClickListener(e->{
            Dialog dialog=new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_edit);
            int height= WindowManager.LayoutParams.WRAP_CONTENT;
            int width=WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width,height);
            dialog.show();
            EditText etName=dialog.findViewById(R.id.etvStudentName);
            Button btnDone=dialog.findViewById(R.id.btnDone);
            try{
                btnDone.setOnClickListener(ec->{
                    String name =etName.getText().toString().trim();
                    Student student = new Student(name);
                    studentAdapter.addStudent(student);
                    dialog.dismiss();
                });
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });

    }
    private void initData(){

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonArrayRequest arrayRequest =new JsonArrayRequest(Request.Method.GET,url,null
                ,res->{
            try{
                for (int i=0;i<res.length();i++){
                    JSONObject object=res.getJSONObject(i);
                    String  id = object.getString("id");
                    String ten=object.getString("name");
                    Student student=new Student(id,ten);
                    students.add(student);
                    System.out.println(student);
                    studentAdapter.notifyDataSetChanged();
                    //là bây giờ liên quan phần adapter thôi
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        },err->{
//            tvShow.setText(err.getMessage());
        });
        requestQueue.add(arrayRequest);
    }
}
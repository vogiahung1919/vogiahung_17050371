package com.example.myaip;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Student> students;
    private String url;

    public StudentAdapter(Context context, ArrayList<Student> students, String url) {
        this.context = context;
        this.students = students;
        this.url = url;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item,parent,false);
        return new StudentViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        System.out.println(student);
        holder.tvName.setText(student.getName());
        holder.btnEdit.setOnClickListener(e->{
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_edit);
            int width= WindowManager.LayoutParams.MATCH_PARENT;
            int height=WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width,height);
            dialog.show();
            EditText edtName =dialog.findViewById(R.id.etvStudentName);
            edtName.setText(student.getName());
            Button btnDone=dialog.findViewById(R.id.btnDone);
            try {
                btnDone.setOnClickListener(ec->{
                    String name= edtName.getText().toString().trim();
                    student.setName(name);
                    updateStudent(student);
                    dialog.dismiss();
                });
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        holder.btnDelete.setOnClickListener(e->{
            deleteStudent(student,position);
        });
    }

    public  void addStudent(Student student) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,url,
                rs->{
                    Toast.makeText(context,"Successfull",Toast.LENGTH_LONG).show();
                    students.add(student);
                    notifyItemInserted(students.size()-1);
                },
                err->{
                    Toast.makeText(context,"Insert fail",Toast.LENGTH_LONG).show();
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("name",student.getName());
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void updateStudent(Student student){
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,url+"/"+student.getId(),
                rs->{
                    Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                },err->{
            Toast.makeText(context, "Update Fail!", Toast.LENGTH_SHORT).show();

            //t thay hop ly roi` ma` :v
            // alo nghe ko
            //nghe- ma` t  noi m khong nghe a`
            // um ko nghe
            //bat loa len di, bam
            //thoi m noi di t nghe :))
            //cang vay ta
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("name",student.getName());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void deleteStudent(Student student,int position){

        StringRequest stringRequest =new StringRequest(Request.Method.DELETE,url+"/"+student.getId(),
                rs->{
                    Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show();
                    students.remove(position);
                    notifyItemChanged(position);
                    notifyItemRangeChanged(position,students.size());
                },
                err->{
                    Toast.makeText(context, "Delete Fail", Toast.LENGTH_SHORT).show();
                });
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }
    @Override
    public int getItemCount() {
        return students.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageButton btnEdit,btnDelete;
        StudentAdapter studentAdapter;
        public StudentViewHolder(@NonNull View itemView, StudentAdapter studentAdapter) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            btnEdit=itemView.findViewById(R.id.btnEdit);
            btnDelete=itemView.findViewById(R.id.btnDelete);
            this.studentAdapter = studentAdapter;
        }
    }
}

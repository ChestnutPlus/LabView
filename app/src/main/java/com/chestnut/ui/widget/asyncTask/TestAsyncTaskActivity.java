package com.chestnut.ui.widget.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chestnut.ui.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

public class TestAsyncTaskActivity extends AppCompatActivity {

    private TextView tvProgress;
    private ProgressBar progressBar;
    private MyAsyncTask myAsyncTask;

    private static class InternalExecutor implements Executor {
        @Override
        public void execute(@NonNull Runnable command) {
            command.run();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_async_task);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        findViewById(R.id.btn).setOnClickListener(v -> {
            if (myAsyncTask==null) {
                myAsyncTask = new MyAsyncTask(TestAsyncTaskActivity.this,progressBar,tvProgress);
            }
            myAsyncTask.execute("start");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myAsyncTask!=null)
            myAsyncTask.cancel(true);
    }

    private static class MyAsyncTask extends AsyncTask<String,Integer,Boolean> {

        /* 为避免内存泄漏，引用应该用弱引用包裹 */
        private String TAG = "MyAsyncTask";
        private WeakReference<Context> contextWeakReference;
        private WeakReference<ProgressBar> progressBarWeakReference;
        private WeakReference<TextView> textViewWeakReference;

        public MyAsyncTask(Context context, ProgressBar progressBar, TextView tvProgress) {
            contextWeakReference = new WeakReference<>(context);
            progressBarWeakReference = new WeakReference<>(progressBar);
            textViewWeakReference = new WeakReference<>(tvProgress);
        }

        /**
         * 运行在UI线程中，用于参数初始化
         * 在 doInBackground 前调用
         */
        @Override
        protected void onPreExecute() {
            Log.i(TAG,"onPreExecute...");
            Context context = contextWeakReference.get();
            if (context!=null)
                Toast.makeText(context,"开始执行",Toast.LENGTH_SHORT).show();
        }

        /**
         * 后台任务处理方法，运行在
         * 子线程，可以做一些耗时任务
         * @param strings 输入参数，对应于泛型：Params
         * @return 结果
         */
        @Override
        protected Boolean doInBackground(String... strings) {
            Log.i(TAG,"doInBackground...");
            int i=0;
            while(i<100){
                if (isCancelled())
                    break;
                i++;
                //任务执行过程进度/参数的回调，对应于泛型：Progress
                publishProgress(i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                }
            }
            //返回值代表最后的结果，对应于泛型：Result
            return true;
        }

        /**
         * 任务执行过程中的更新，
         * 由子方法：publishProgress()
         * 触发，运行在UI线程中
         * @param values 参数
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            TextView tvProgress = textViewWeakReference.get();
            ProgressBar progressBar = progressBarWeakReference.get();
            if (tvProgress!=null)
                tvProgress.setText(values[0]+"%");
            if (progressBar!=null)
                progressBar.setProgress(values[0]);
        }

        /**
         * 执行结束的回调，运行在UI线程
         * @param bool 参数
         */
        @Override
        protected void onPostExecute(Boolean bool) {
            Log.i(TAG,"onPostExecute, " + bool);
            Context context = contextWeakReference.get();
            if (context!=null)
                Toast.makeText(context,"执行完毕",Toast.LENGTH_SHORT).show();
        }

        /**
         * 执行 task.cancel() 方法后，
         * 会回调此方法
         */
        @Override
        protected void onCancelled() {
            Log.i(TAG,"onCancelled");
        }
    }
}

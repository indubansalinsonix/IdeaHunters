package com.ideahunters.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ideahunters.R;
import com.ideahunters.customwidgets.RoundedImageView.RoundedImageView;
import com.ideahunters.model.Comment;
import com.ideahunters.presenter.ReportCommentPresenter;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 6/2/17.
 */

public class CommentListAdapter1 extends BaseAdapter implements Constants, ReportCommentPresenter.ReportCommentPresenterListener {

    private final ReportCommentPresenter reportPresenter;
    ArrayList<Comment> addme;
    private Activity activity;
    ProgressBar progressBar;
    TextView report_spam_comment;
    private String sug_id;

    public CommentListAdapter1(Activity descriptionActivity, ArrayList<Comment> addme, String sug_id) {
        this.addme = addme;
        this.activity = descriptionActivity;
        this.sug_id = sug_id;
        reportPresenter = new ReportCommentPresenter(this, activity);

    }


    @Override
    public int getCount() {
        return addme.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        convertView = inflater.inflate(R.layout.comment_item, null);
        TextView comments_item = (TextView) convertView.findViewById(R.id.comment_item);
        TextView added = (TextView) convertView.findViewById(R.id.comment_time);
        TextView posted_by = (TextView) convertView.findViewById(R.id.posted_by);
        posted_by.setText(addme.get(position).getSubmittedBy());
        progressBar = (ProgressBar) convertView.findViewById(R.id.loader);
        comments_item.setText(addme.get(position).getComments());
        added.setText(addme.get(position).getCreatedAt());

        report_spam_comment = (TextView) convertView.findViewById(R.id.report_comment);
        progressBar.setVisibility(View.GONE);
        posted_by.setText(addme.get(position).getSubmittedBy());
        comments_item.setText(addme.get(position).getComments());
        String formatted_date = Singleton.getInstance().parseDateToddMMyyyy(activity, addme.get(position).getCreatedAt());
        added.setText(formatted_date);
        if (TextUtils.equals(addme.get(position).getCommentedBy(), Singleton.getInstance().getValue(activity, USER_ID))) {
            report_spam_comment.setVisibility(View.GONE);
        } else {
            report_spam_comment.setVisibility(View.VISIBLE);

        }
        if (TextUtils.equals(addme.get(position).getReport(), "1")) {
            Log.e("click", "1");
            report_spam_comment.setClickable(false);
            report_spam_comment.setEnabled(false);
            report_spam_comment.setPaintFlags(report_spam_comment.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            Log.e("click", "0");

            report_spam_comment.setClickable(true);
            report_spam_comment.setEnabled(true);
            report_spam_comment.setPaintFlags(report_spam_comment.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        }


        final View finalConvertView1 = convertView;
        report_spam_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar = (ProgressBar) finalConvertView1.findViewById(R.id.loader);

                report_spam_comment = (TextView) finalConvertView1.findViewById(R.id.report_comment);

                if (TextUtils.equals(addme.get(position).getReport(), "0")) {
                    progressBar.setVisibility(View.VISIBLE);

                    reportPresenter.reportComment(sug_id, addme.get(position).getCommentId(), report_spam_comment, progressBar);

                }
            }
        });

        return convertView;
    }

    @Override
    public void onSuccessfulReport(String message, TextView report_spam_comment, ProgressBar progressBar) {
        report_spam_comment.setClickable(false);
        report_spam_comment.setEnabled(false);
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        report_spam_comment.setPaintFlags(report_spam_comment.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }

    @Override
    public void onUnSuccessfulReport(String message, TextView report_spam_comment, ProgressBar progressBar) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);

        report_spam_comment.setPaintFlags(report_spam_comment.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

    }


}
package com.zjzy.morebit.pojo;

import java.io.Serializable;

public class UnreadInforBean implements Serializable {

        /**
         * fs : false
         * income : false
         * feedback : false
         * activity : false
         * system : true
         */

        private boolean fs;
        private boolean income;
        private boolean feedback;
        private boolean activity;
        private boolean system;

        public boolean isFs() {
            return fs;
        }

        public void setFs(boolean fs) {
            this.fs = fs;
        }

        public boolean isIncome() {
            return income;
        }

        public void setIncome(boolean income) {
            this.income = income;
        }

        public boolean isFeedback() {
            return feedback;
        }

        public void setFeedback(boolean feedback) {
            this.feedback = feedback;
        }

        public boolean isActivity() {
            return activity;
        }

        public void setActivity(boolean activity) {
            this.activity = activity;
        }

        public boolean isSystem() {
            return system;
        }

        public void setSystem(boolean system) {
            this.system = system;
        }
    }



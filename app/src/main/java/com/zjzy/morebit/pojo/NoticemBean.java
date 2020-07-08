package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

public class NoticemBean implements Serializable {
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * type : 4
             * msg :
             */

            private int type;
            private String msg;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }
        }
    }


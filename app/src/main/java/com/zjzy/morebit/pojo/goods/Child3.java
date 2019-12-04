package com.zjzy.morebit.pojo.goods;

import java.io.Serializable;

public   class Child3 implements Serializable {
            /**
             * id : 3
             * category_id : 1
             * pid : 2
             * name : 连衣裙
             * pictrue : http://img.haodanku.com/89937f347f81f5c5539f9da9b35b7a62-600
             */

            private int id;
            private int category_id;
            private int pid;
            private String name="";
            private String picture="";

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCategory_id() {
                return category_id;
            }

            public void setCategory_id(int category_id) {
                this.category_id = category_id;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPictrue() {
                return picture;
            }

            public void setPictrue(String pictrue) {
                this.picture = pictrue;
            }
        }
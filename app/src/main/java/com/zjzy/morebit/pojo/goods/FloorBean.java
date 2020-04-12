package com.zjzy.morebit.pojo.goods;

import java.io.Serializable;

public class FloorBean implements Serializable {


        /**
         * flashSalePic : null
         * noviceTutorialPic : null
         * noviceTutorial : 1
         */

        private String flashSalePic;
        private String noviceTutorialPic;
        private String noviceTutorial;
        private String zeroActivityRule;

    public String getZeroActivityRule() {
        return zeroActivityRule;
    }

    public void setZeroActivityRule(String zeroActivityRule) {
        this.zeroActivityRule = zeroActivityRule;
    }

    public String getFlashSalePic() {
            return flashSalePic;
        }

        public void setFlashSalePic(String flashSalePic) {
            this.flashSalePic = flashSalePic;
        }

        public String getNoviceTutorialPic() {
            return noviceTutorialPic;
        }

        public void setNoviceTutorialPic(String noviceTutorialPic) {
            this.noviceTutorialPic = noviceTutorialPic;
        }

        public String getNoviceTutorial() {
            return noviceTutorial;
        }

        public void setNoviceTutorial(String noviceTutorial) {
            this.noviceTutorial = noviceTutorial;
        }
    }


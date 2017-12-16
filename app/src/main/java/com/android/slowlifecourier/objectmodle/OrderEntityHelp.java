package com.android.slowlifecourier.objectmodle;

/**
 * Created by xxxloli on 2017/12/16.
 */

public class OrderEntityHelp {

    static class Head implements Cloneable{
        public  Face face;

        public Head() {}
        public Head(Face face){this.face = face;}
        @Override
        protected Object clone() throws CloneNotSupportedException {
            //return super.clone();
            Head newHead = (Head) super.clone();
            newHead.face = (Face) this.face.clone();
            return newHead;
        }
    }

    static class Face implements Cloneable{
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
}

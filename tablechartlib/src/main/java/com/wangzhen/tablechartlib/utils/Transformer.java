
package com.wangzhen.tablechartlib.utils;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

import java.util.List;

/**
 * Transformer class that contains all matrices and is responsible for
 * transforming values into pixels on the screen and backwards.
 *
 * 1.将原始value值转化为px值得装换类，
 * 2.和mViewPortHandler配合计算出拖拽和缩放后的px值
 *
 * @author Philipp Jahoda
 */
public class Transformer {

    /**
     * matrix to map the values to the screen pixels
     */
    protected Matrix mMatrixValueToPx = new Matrix();

    /**
     * matrix for handling the different offsets of the chart
     */
    protected Matrix mMatrixOffset = new Matrix();

    protected ViewPortHandler mViewPortHandler;

    public Transformer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }

    /**
     * Prepares the matrix that transforms values to pixels. Calculates the
     * scale factors from the charts size and offsets.
     * 初始化 将原始数据value 对应到 屏幕px的矩阵，原理很简单，
     * 比如xValue=[10,90],yValue = [20,100],屏幕的大小为[0,0,100,200]
     * 先将xValue 和 yValue的最小值作为起始点 matrix.postTranslate(-xChartMin, -yChartMin);
     * 然后计算出需要缩放的比例 scaleX= 100/(90-10)  scaleY = 200/(100 - 20)
     * postScale(scaleX, -scaleY)  注意-scaleY是为了将屏幕坐标系转为普通的坐标系
     *
     * @param xChartMin
     * @param deltaX
     * @param deltaY
     * @param yChartMin
     */
    public void prepareMatrixValuePx(float xChartMin, float deltaX, float deltaY, float yChartMin) {

        float scaleX = (float) ((mViewPortHandler.contentWidth()) / deltaX);
        float scaleY = (float) ((mViewPortHandler.contentHeight()) / deltaY);

        if (Float.isInfinite(scaleX)) {
            scaleX = 0;
        }
        if (Float.isInfinite(scaleY)) {
            scaleY = 0;
        }

        // setup all matrices
        mMatrixValueToPx.reset();
        mMatrixValueToPx.postTranslate(-xChartMin, -yChartMin);
        mMatrixValueToPx.postScale(scaleX, -scaleY);
    }

    /**
     * Prepares the matrix that contains all offsets.
     *
     * @param inverted
     */
    public void prepareMatrixOffset(boolean inverted) {

        mMatrixOffset.reset();

        // offset.postTranslate(mOffsetLeft, getHeight() - mOffsetBottom);

        if (!inverted)
            mMatrixOffset.postTranslate(mViewPortHandler.offsetLeft(),
                    mViewPortHandler.getChartHeight() - mViewPortHandler.offsetBottom());
        else {
            mMatrixOffset
                    .setTranslate(mViewPortHandler.offsetLeft(), -mViewPortHandler.offsetTop());
            mMatrixOffset.postScale(1.0f, -1.0f);
        }
    }

    protected float[] valuePointsForGenerateTransformedValuesScatter = new float[1];

    /**
     * Transforms an List of Entry into a float array containing the x and
     * y values transformed with all matrices for the SCATTERCHART.
     *
     * @param data
     * @return
     */

    protected float[] valuePointsForGenerateTransformedValuesCandle = new float[1];


    /**
     * transform a path with all the given matrices VERY IMPORTANT: keep order
     * to value-touch-offset
     *
     * @param path
     */
    public void pathValueToPixel(Path path) {

        path.transform(mMatrixValueToPx);
        path.transform(mViewPortHandler.getMatrixTouch());
        path.transform(mMatrixOffset);
    }

    /**
     * Transforms multiple paths will all matrices.
     *
     * @param paths
     */
    public void pathValuesToPixel(List<Path> paths) {

        for (int i = 0; i < paths.size(); i++) {
            pathValueToPixel(paths.get(i));
        }
    }

    /**
     * Transform an array of points with all matrices. VERY IMPORTANT: Keep
     * matrix order "value-touch-offset" when transforming.
     *
     * @param pts
     */
    public void pointValuesToPixel(float[] pts) {
        long s = System.currentTimeMillis();
        mMatrixValueToPx.mapPoints(pts);
        mViewPortHandler.getMatrixTouch().mapPoints(pts);
        mMatrixOffset.mapPoints(pts);
//        Log.e("mapPoints","count: "+ pts.length+",mapPoints time:" +(System.currentTimeMillis() - s));
    }

    /**
     * Transform a rectangle with all matrices.
     *
     * @param r
     */
    public void rectValueToPixel(RectF r) {

        mMatrixValueToPx.mapRect(r);
        mViewPortHandler.getMatrixTouch().mapRect(r);
        mMatrixOffset.mapRect(r);
    }

    /**
     * Transform a rectangle with all matrices with potential animation phases.
     *
     * @param r
     * @param phaseY
     */
    public void rectToPixelPhase(RectF r, float phaseY) {

        // multiply the height of the rect with the phase
        r.top *= phaseY;
        r.bottom *= phaseY;

        mMatrixValueToPx.mapRect(r);
        mViewPortHandler.getMatrixTouch().mapRect(r);
        mMatrixOffset.mapRect(r);
    }

    public void rectToPixelPhaseHorizontal(RectF r, float phaseY) {

        // multiply the height of the rect with the phase
        r.left *= phaseY;
        r.right *= phaseY;

        mMatrixValueToPx.mapRect(r);
        mViewPortHandler.getMatrixTouch().mapRect(r);
        mMatrixOffset.mapRect(r);
    }

    /**
     * Transform a rectangle with all matrices with potential animation phases.
     *
     * @param r
     */
    public void rectValueToPixelHorizontal(RectF r) {

        mMatrixValueToPx.mapRect(r);
        mViewPortHandler.getMatrixTouch().mapRect(r);
        mMatrixOffset.mapRect(r);
    }

    /**
     * Transform a rectangle with all matrices with potential animation phases.
     *
     * @param r
     * @param phaseY
     */
    public void rectValueToPixelHorizontal(RectF r, float phaseY) {

        // multiply the height of the rect with the phase
        r.left *= phaseY;
        r.right *= phaseY;

        mMatrixValueToPx.mapRect(r);
        mViewPortHandler.getMatrixTouch().mapRect(r);
        mMatrixOffset.mapRect(r);
    }

    /**
     * transforms multiple rects with all matrices
     *
     * @param rects
     */
    public void rectValuesToPixel(List<RectF> rects) {

        Matrix m = getValueToPixelMatrix();

        for (int i = 0; i < rects.size(); i++)
            m.mapRect(rects.get(i));
    }

    protected Matrix mPixelToValueMatrixBuffer = new Matrix();

    /**
     * Transforms the given array of touch positions (pixels) (x, y, x, y, ...)
     * into values on the chart.
     *
     * @param pixels
     */
    public void pixelsToValue(float[] pixels) {

        Matrix tmp = mPixelToValueMatrixBuffer;
        tmp.reset();

        // invert all matrixes to convert back to the original value
        mMatrixOffset.invert(tmp);
        tmp.mapPoints(pixels);

        mViewPortHandler.getMatrixTouch().invert(tmp);
        tmp.mapPoints(pixels);

        mMatrixValueToPx.invert(tmp);
        tmp.mapPoints(pixels);
    }

    /**
     * buffer for performance
     */
    float[] ptsBuffer = new float[2];

    /**
     * Returns a recyclable MPPointD instance.
     * returns the x and y values in the chart at the given touch point
     * (encapsulated in a MPPointD). This method transforms pixel coordinates to
     * coordinates / values in the chart. This is the opposite method to
     * getPixelForValues(...).
     *
     * @param x
     * @param y
     * @return
     */
    public MPPointD getValuesByTouchPoint(float x, float y) {

        MPPointD result = MPPointD.getInstance(0, 0);
        getValuesByTouchPoint(x, y, result);
        return result;
    }

    public void getValuesByTouchPoint(float x, float y, MPPointD outputPoint) {

        ptsBuffer[0] = x;
        ptsBuffer[1] = y;

        pixelsToValue(ptsBuffer);

        outputPoint.x = ptsBuffer[0];
        outputPoint.y = ptsBuffer[1];
    }

    /**
     * 根据传入的x，y的坐标值，获取对应的value值
     *
     * @param x
     * @param y
     * @return
     */
    public MPPointD getPixelForValues(float x, float y) {

        ptsBuffer[0] = x;
        ptsBuffer[1] = y;

        pointValuesToPixel(ptsBuffer);

        double xPx = ptsBuffer[0];
        double yPx = ptsBuffer[1];

        return MPPointD.getInstance(xPx, yPx);
    }

    public Matrix getValueMatrix() {
        return mMatrixValueToPx;
    }

    public Matrix getOffsetMatrix() {
        return mMatrixOffset;
    }

    private Matrix mMBuffer1 = new Matrix();

    public Matrix getValueToPixelMatrix() {
        mMBuffer1.set(mMatrixValueToPx);
        mMBuffer1.postConcat(mViewPortHandler.mMatrixTouch);
        mMBuffer1.postConcat(mMatrixOffset);
        return mMBuffer1;
    }

    private Matrix mMBuffer2 = new Matrix();

    public Matrix getPixelToValueMatrix() {
        getValueToPixelMatrix().invert(mMBuffer2);
        return mMBuffer2;
    }
}

package com.stebanramos.convertautomata;


import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class BubbleChart extends View {

    private static final int DEFAULT_PADDING_LENGTH = 25;
    private static final int DEFAULT_DATAAXIS_NGRID = 4;
    private static final float DEFAULT_EMPTYPX = 5;

    private Activity ctx;

    private RectF rect;

    private int dataMaxXValue = 0;

    private int dataMaxYValue = 0;

    private float dataMinYValue = 0;

    private float minPoint = 0;

    private int titleXPx = 0;

    private int titleYPx = 0;

    /**
     * Espacio de línea horizontal de cuadrícula
     */
    private float xAxisGridGap;

    /**
     * Espacio de línea vertical de cuadrícula
     */
    private float yAxisGridGap;

    /**
     * El número de líneas de cuadrícula en el sistema de coordenadas.
     */
    private int xAxisNGrid;

    /**
     * Número de líneas verticales de cuadrícula en el sistema de coordenadas
     */
    private int yAxisNGrid;

    private String title;

    private String titleFont;

    private String subTitle;

    private String subTitleFont;

    private String dataTitleFont;

    private String titleX;

    private String titleXFont;

    private String titleY;

    private String titleYFont;

    private ArrayList<Integer> colors;

    private LinkedHashMap<String, ArrayList<String[]>> datas;

    public BubbleChart(Activity ctx) {
        super(ctx);
        this.ctx = ctx;
        int height = 0;
        Rect frame = new Rect();
        ctx.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        height = frame.top;
        this.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        this.getBackground().setAlpha(180);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleFont(String titleFont) {
        this.titleFont = titleFont;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setSubTitleFont(String subTitleFont) {
        this.subTitleFont = subTitleFont;
    }

    public void setDataTitleFont(String dataTitleFont) {
        this.dataTitleFont = dataTitleFont;
    }

    public void setTitleX(String titleX) {
        this.titleX = titleX;
    }

    public void setTitleXFont(String titleXFont) {
        this.titleXFont = titleXFont;
    }

    public void setTitleY(String titleY) {
        this.titleY = titleY;
    }

    public void setTitleYFont(String titleYFont) {
        this.titleYFont = titleYFont;
    }

    public void setDatas(LinkedHashMap<String, ArrayList<String[]>> datas) {
        this.datas = datas;
    }

    public void setColor(ArrayList<Integer> colors) {
        this.colors = colors;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawChart(canvas);
    }

    /**
     * Dibujar tabla de burbujas
     *
     * @param canvas Objeto de lienzo
     */
    private void drawChart(Canvas canvas) {
        Rect leftRect = drawTitle(canvas);
        Rect rightRect = drawDataTitle(canvas, leftRect);
        int titleHeight = leftRect.bottom > rightRect.bottom ? leftRect.bottom : rightRect.bottom;
        rect = new RectF(0, titleHeight, this.getWidth(), this.getHeight());
        initChartDatas();
        if (!titleX.equals("")) {
            drawTitleX(canvas);
        }
        if (!titleY.equals("")) {
            drawTitleY(canvas);
        }
        drawXAxisScaleLine(canvas);
        drawYAxisScaleLine(canvas);
        drawXAxisLabel(canvas);
        drawYAxisLabel(canvas);
        drawDiagram(canvas);
        drawXAxisLine(canvas);
        drawYAxisLine(canvas);
    }

    /**
     * Dibujar principales y subtítulos
     *
     * @param canvas Objeto de lienzo
     */
    private Rect drawTitle(Canvas canvas) {
        Paint paint = new Paint();
        float titleTextSize = 30;
        if (titleFont != null && !"".equals(titleFont)) {
            if (titleFont.indexOf(";") != -1) {
                String[] strs = titleFont.split(";");
                for (String str : strs) {
                    if (str.startsWith("font-size")) {
                        int index = str.indexOf(":");
                        if (index == -1)
                            continue;
                        String s = str.substring(index + 1).trim();
                        paint.setTextSize(Float.parseFloat(s.replace("px", "").trim()));
                        titleTextSize = Float.parseFloat(s.replace("px", "").trim());
                    } else if (str.startsWith("style")) {
                        int index = str.indexOf(":");
                        if (index == -1)
                            continue;
                        String s = str.substring(index + 1).trim();
                        if ("bold".equals(s)) {
                            paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        } else if ("italic".equals(s)) {
                            paint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                        } else {
                            if (s.indexOf(",") != -1) {
                                if ("bold".equals(s.split(",")[0]) && "italic".equals(s.split(",")[1])) {
                                    paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                                }
                                if ("bold".equals(s.split(",")[1]) && "italic".equals(s.split(",")[0])) {
                                    paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                                }
                            }
                        }
                    } else if (str.startsWith("color")) {
                        int index = str.indexOf(":");
                        if (index == -1)
                            continue;
                        String s = str.substring(index + 1).trim();

                        int r = Integer.parseInt(s.substring(0, 3));
                        int g = Integer.parseInt(s.substring(3, 6));
                        int b = Integer.parseInt(s.substring(6, 9));
                        paint.setColor(Color.rgb(r, g, b));

                    }
                }
            }
        }
        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawText(title, DEFAULT_PADDING_LENGTH, paint.getFontMetrics().bottom - paint.getFontMetrics().top, paint);
        int titleWidth = (int) paint.measureText(title) + DEFAULT_PADDING_LENGTH * 2;
        int titleHeight = (int) (paint.getFontMetrics().bottom - paint.getFontMetrics().top + titleTextSize * 0.75);

        paint = new Paint();
        float subTitleTextSize = 23;
        if (subTitleFont != null && !"".equals(subTitleFont)) {
            if (subTitleFont.indexOf(";") != -1) {
                String[] strs = subTitleFont.split(";");
                for (String str : strs) {
                    if (str.startsWith("font-size")) {
                        int index = str.indexOf(":");
                        if (index == -1) continue;
                        String s = str.substring(index + 1).trim();
                        paint.setTextSize(Float.parseFloat(s.replace("px", "").trim()));
                        subTitleTextSize = Float.parseFloat(s.replace("px", "").trim());
                    } else if (str.startsWith("style")) {
                        int index = str.indexOf(":");
                        if (index == -1)
                            continue;
                        String s = str.substring(index + 1).trim();
                        if ("bold".equals(s)) {
                            paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        } else if ("italic".equals(s)) {
                            paint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                        } else {
                            if (s.indexOf(",") != -1) {
                                if ("bold".equals(s.split(",")[0]) && "italic".equals(s.split(",")[1])) {
                                    paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                                }
                                if ("bold".equals(s.split(",")[1]) && "italic".equals(s.split(",")[0])) {
                                    paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                                }
                            }
                        }
                    } else if (str.startsWith("color")) {
                        int index = str.indexOf(":");
                        if (index == -1)
                            continue;
                        String s = str.substring(index + 1).trim();

                        int r = Integer.parseInt(s.substring(0, 3));
                        int g = Integer.parseInt(s.substring(3, 6));
                        int b = Integer.parseInt(s.substring(6, 9));
                        paint.setColor(Color.rgb(r, g, b));

                    }
                }
            }
        }
        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawText(subTitle, DEFAULT_PADDING_LENGTH, paint.getFontMetrics().bottom - paint.getFontMetrics().top + titleHeight, paint);
        int subTitleWidth = (int) paint.measureText(subTitle) + DEFAULT_PADDING_LENGTH * 2;
        int subTitleHeight = (int) (paint.getFontMetrics().bottom - paint.getFontMetrics().top + subTitleTextSize * 0.75);

        int width = titleWidth > subTitleWidth ? titleWidth : subTitleWidth;
        int height = titleHeight + subTitleHeight;
        return new Rect(0, 0, width, height);
    }

    private Rect drawDataTitle(Canvas canvas, Rect rectF) {
        Paint paint = new Paint();
        float dataTitleTextSize = 23;
        if (dataTitleFont != null && !"".equals(dataTitleFont)) {
            if (dataTitleFont.indexOf(";") != -1) {
                String[] strs = dataTitleFont.split(";");
                for (String str : strs) {
                    if (str.startsWith("font-size")) {
                        int index = str.indexOf(":");
                        if (index == -1) continue;
                        String s = str.substring(index + 1).trim();
                        paint.setTextSize(Float.parseFloat(s.replace("px", "").trim()));
                        dataTitleTextSize = Float.parseFloat(s.replace("px", "").trim());
                    } else if (str.startsWith("style")) {
                        int index = str.indexOf(":");
                        if (index == -1)
                            continue;
                        String s = str.substring(index + 1).trim();
                        if ("bold".equals(s)) {
                            paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        } else if ("italic".equals(s)) {
                            paint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                        } else {
                            if (s.indexOf(",") != -1) {
                                if ("bold".equals(s.split(",")[0]) && "italic".equals(s.split(",")[1])) {
                                    paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                                }
                                if ("bold".equals(s.split(",")[1]) && "italic".equals(s.split(",")[0])) {
                                    paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                                }
                            }
                        }
                    } else if (str.startsWith("color")) {
                        int index = str.indexOf(":");
                        if (index == -1)
                            continue;
                        String s = str.substring(index + 1).trim();

                        int r = Integer.parseInt(s.substring(0, 3));
                        int g = Integer.parseInt(s.substring(3, 6));
                        int b = Integer.parseInt(s.substring(6, 9));
                        paint.setColor(Color.rgb(r, g, b));

                    }
                }
            }
        }
        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);
        Rect rect = new Rect(rectF);
        rect.set(rect.right + DEFAULT_PADDING_LENGTH, rect.top + 15, this.getWidth() - DEFAULT_PADDING_LENGTH, rect.bottom);
        if (datas.size() > 0) {
            String[] list = datas.keySet().toArray(new String[0]);
            Integer[] lengths = new Integer[list.length];
            for (int i = 0; i < list.length; i++) {
                lengths[i] = (int) paint.measureText(list[i]);
            }

            int itemWidth = lengths[0];
            int itemHeight = (int) (paint.getFontMetrics().bottom - paint.getFontMetrics().top);
            int lineHeight = (int) (paint.getFontMetrics().bottom - paint.getFontMetrics().top + dataTitleTextSize * 0.75);
            int colsCount = rect.width() / (itemWidth + DEFAULT_PADDING_LENGTH * 2);
            int rowsCount = list.length / colsCount + (list.length % colsCount > 0 ? 1 : 0);
            rect.set(rect.right - (itemWidth + DEFAULT_PADDING_LENGTH * 2) * colsCount, rect.top, rect.right, rect.top + lineHeight * rowsCount);
            for (int i = 0; i < rowsCount; i++) {
                if (i < rowsCount - 1) {
                    for (int j = 0; j < colsCount; j++) {
                        canvas.drawText(list[i * colsCount + j], rect.left + (itemWidth + DEFAULT_PADDING_LENGTH * 2) * j + DEFAULT_PADDING_LENGTH * 2, rect.top + lineHeight * i + itemHeight, paint);
                        Paint p = new Paint();
                        p.setColor(colors.get(i * colsCount + j));
                        p.setStyle(Style.FILL_AND_STROKE);
                        p.setAntiAlias(true);
                        canvas.drawCircle(rect.left + (itemWidth + DEFAULT_PADDING_LENGTH * 2) * j + DEFAULT_PADDING_LENGTH, rect.top + lineHeight * i + lineHeight / 2, 13, p);
                    }
                } else {
                    int index = list.length - (rowsCount - 1) * colsCount;
                    for (int j = 0; j < index; j++) {
                        canvas.drawText(list[i * colsCount + j], rect.left + (itemWidth + DEFAULT_PADDING_LENGTH * 2) * (j + colsCount - index) + DEFAULT_PADDING_LENGTH * 2, rect.top + lineHeight * i + itemHeight, paint);
                        Paint p = new Paint();
                        p.setColor(colors.get(i * colsCount + j));
                        p.setStyle(Style.FILL_AND_STROKE);
                        p.setAntiAlias(true);
                        canvas.drawCircle(rect.left + (itemWidth + DEFAULT_PADDING_LENGTH * 2) * (j + colsCount - index) + DEFAULT_PADDING_LENGTH, rect.top + lineHeight * i + lineHeight / 2, 13, p);
                    }
                }
            }
        }
        return rect;
    }

    /**
     * Operación inicial
     */
    private void initChartDatas() {
        obtainDataMaxValue();
        minPoint();
        titleXPx = obtainPaintXYHeight(titleXFont);
        titleYPx = obtainPaintXYHeight(titleYFont);
        float originX = 0;
        float originY = 0;
        if (titleY.equals("")) {
            originX = rect.left + titleYPx + DEFAULT_EMPTYPX * 2;
        } else {
            originX = rect.left + (titleYPx + DEFAULT_EMPTYPX) * 2;
        }
        if (titleX.equals("")) {
            originY = rect.bottom - titleXPx - DEFAULT_EMPTYPX * 2;
        } else {
            originY = rect.bottom - (titleXPx + DEFAULT_EMPTYPX) * 2;
        }
        rect = new RectF(originX, rect.top + DEFAULT_EMPTYPX * 2, rect.right - DEFAULT_EMPTYPX * 5, originY);
        xAxisNGrid = DEFAULT_DATAAXIS_NGRID;
        yAxisNGrid = DEFAULT_DATAAXIS_NGRID;
        xAxisGridGap = rect.width() / xAxisNGrid;
        yAxisGridGap = rect.height() / yAxisNGrid;
    }

    /**
     * Establecer el valor máximo del punto de coordenadas del eje XY
     */
    private void obtainDataMaxValue() {
        float maxX = 0;
        float maxY = 0;
        String[] ids = datas.keySet().toArray(new String[0]);
        for (String id : ids) {
            ArrayList<String[]> list = datas.get(id);
            for (int i = 0; i < list.size(); i++) {
                float countX = Float.parseFloat(list.get(i)[3]);
                float countY = Float.parseFloat(list.get(i)[4]);
                maxX = maxX > countX ? maxX : countX;
                maxY = maxY > countY ? maxY : countY;
            }
        }

    }

    /**
     * Establecer el valor máximo del punto de coordenadas del eje XY
     */
    private void minPoint() {
        String[] ids = datas.keySet().toArray(new String[0]);
        boolean first = true;
        for (String id : ids) {
            ArrayList<String[]> list = datas.get(id);
            for (int i = 0; i < list.size(); i++) {
                float countX = Float.parseFloat(list.get(i)[5]);
                float minY = Float.parseFloat(list.get(i)[4]);
                if (first) {
                    first = false;
                    minPoint = countX;
                    dataMinYValue = minY;
                } else {
                    minPoint = minPoint < countX ? minPoint : countX;
                    dataMinYValue = dataMinYValue < minY ? dataMinYValue : minY;
                }
            }
        }

    }

    /**
     * Dibujar puntos de gráfico de burbujas
     *
     * @param canvas Objeto de lienzo
     */
    private void drawDiagram(Canvas canvas) {
        HashMap<String, ArrayList<Point>> pointsMap = new HashMap<String, ArrayList<Point>>();
        HashMap<String, Integer> colorsMap = new HashMap<String, Integer>();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Style.FILL_AND_STROKE);
        String[] ids = datas.keySet().toArray(new String[0]);
        int i = 0;
        for (String id : ids) {
            List<String[]> list = datas.get(id);
            for (int j = 0; j < list.size(); j++) {
                int x = (int) (rect.left + Float.parseFloat(list.get(j)[3]) / dataMaxXValue * rect.width());
                int y = (int) (rect.bottom - (Float.parseFloat(list.get(j)[4]) - dataMinYValue) / (dataMaxYValue - dataMinYValue) * rect.height());
                Point point = new Point(x, y);
                paint.setColor(colors.get(i));
                paint.setAlpha(200);
                float pointSize = (float) (Math.sqrt(Integer.parseInt(list.get(j)[5]) / minPoint) * 8);
                canvas.drawCircle(point.x, point.y, pointSize, paint);
                paint.setTextSize(18);
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                String strName = list.get(j)[2];
                float strLength = paint.measureText(strName);
                String str = list.get(j)[4];
                float val = Float.parseFloat(str);
                int x1 = (int) (rect.left + Float.parseFloat(list.get(j)[3]) / dataMaxXValue * rect.width());
                int y1 = (int) (rect.bottom - (val - dataMinYValue) / (dataMaxYValue - dataMinYValue) * rect.height());
                Point point1 = new Point(x1, y1);
                paint.setColor(ctx.getResources().getColor(R.color.black));
                canvas.drawText(strName, point1.x - strLength / 2, point1.y - 15, paint);
            }
            i++;
        }

    }

    /**
     * Dibuje el título del eje horizontal del eje de coordenadas
     *
     * @param canvas Objeto de lienzo
     */
    private void drawTitleX(Canvas canvas) {
        Paint paint = obtainPaintXYPaint(titleXFont);
        canvas.drawText(titleX, rect.left + (rect.width() - paint.measureText(titleX)) / 2, rect.bottom + titleXPx * 2, paint);
    }

    /**
     * Dibuje el título del texto del eje vertical del eje de coordenadas
     *
     * @param canvas Objeto de lienzo
     */
    private void drawTitleY(Canvas canvas) {
        Paint paint = obtainPaintXYPaint(titleYFont);
        Path path = new Path();
        path.moveTo(rect.left - titleYPx * 2, rect.top + (rect.height() - paint.measureText(titleY)) / 2);
        path.lineTo(rect.left - titleYPx * 2, rect.bottom - (rect.height() - paint.measureText(titleY)) / 2);
        canvas.drawTextOnPath(titleY, path, 0, 0, paint);
    }

    /**
     * Dibuja pequeñas líneas horizontales en la cuadrícula (puntos donde existen datos)
     *
     * @param canvas Objeto de lienzo
     */
    private void drawXAxisScaleLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        for (int i = 1; i <= xAxisNGrid * 2; i++) {
            canvas.drawLine(rect.left + i * xAxisGridGap / 2, rect.bottom,
                    rect.left + i * xAxisGridGap / 2, rect.bottom + DEFAULT_EMPTYPX, paint);
        }
    }

    /**
     * Dibuje pequeñas líneas verticales de la cuadrícula en el eje de coordenadas (puntos donde existen datos)
     *
     * @param canvas Objeto de lienzo
     */
    private void drawYAxisScaleLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        for (int i = 1; i <= yAxisNGrid; i++)
            canvas.drawLine(rect.left, rect.bottom - i * yAxisGridGap,
                    rect.left - DEFAULT_EMPTYPX, rect.bottom - i * yAxisGridGap, paint);
    }

    /**
     * Dibujar texto de etiqueta horizontal
     *
     * @param canvas Objeto de lienzo
     */
    private void drawXAxisLabel(Canvas canvas) {
        Paint paint = obtainPaintXYPaint(titleXFont);
        float textStartPx = 0;
        String text;
        for (int i = 0; i <= xAxisNGrid; i++) {
            int singleVal = dataMaxXValue / DEFAULT_DATAAXIS_NGRID;
            text = String.valueOf(singleVal * i);
            textStartPx = paint.measureText(text) / 2;
            canvas.drawText(text, rect.left - textStartPx + i * xAxisGridGap, rect.bottom + titleXPx, paint);
        }
    }

    /**
     * Dibujar texto de etiqueta vertical
     *
     * @param canvas Objeto de lienzo
     */
    private void drawYAxisLabel(Canvas canvas) {
        Paint paint = obtainPaintXYPaint(titleYFont);
        String text;
        for (int i = 0; i <= yAxisNGrid; i++) {
            int singleVal = (int) ((dataMaxYValue - dataMinYValue) / DEFAULT_DATAAXIS_NGRID);
            text = String.valueOf(singleVal * i + (int) dataMinYValue);
            Path path = new Path();
            path.moveTo(rect.left - titleYPx, rect.bottom - i * yAxisGridGap - paint.measureText(text) / 2);
            path.lineTo(rect.left - titleYPx, rect.bottom - i * yAxisGridGap + paint.measureText(text) / 2);
            canvas.drawTextOnPath(text, path, 0, 0, paint);
        }
    }

    private int obtainPaintXYHeight(String font) {
        Paint paint = obtainPaintXYPaint(font);
        return (int) (paint.getFontMetrics().bottom - paint.getFontMetrics().top);
    }

    /**
     * Devuelve el objeto Paint que dibuja el texto del punto de coordenadas XY
     *
     * @param font Tamano del texto
     * @return Objeto de pintura
     */
    private Paint obtainPaintXYPaint(String font) {
        Paint paint = new Paint();
        if (font != null && !"".equals(font)) {
            if (font.indexOf(";") != -1) {
                String[] strs = font.split(";");
                for (String str : strs) {
                    if (str.startsWith("font-size")) {
                        int index = str.indexOf(":");
                        if (index == -1)
                            continue;
                        String s = str.substring(index + 1).trim();
                        paint.setTextSize(Float.parseFloat(s.replace("px", "").trim()));
                    } else if (str.startsWith("style")) {
                        int index = str.indexOf(":");
                        if (index == -1)
                            continue;
                        String s = str.substring(index + 1).trim();
                        if ("bold".equals(s)) {
                            paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        } else if ("italic".equals(s)) {
                            paint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                        } else {
                            if (s.indexOf(",") != -1) {
                                if ("bold".equals(s.split(",")[0]) && "italic".equals(s.split(",")[1])) {
                                    paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                                }
                                if ("bold".equals(s.split(",")[1]) && "italic".equals(s.split(",")[0])) {
                                    paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                                }
                            }
                        }
                    } else if (str.startsWith("color")) {
                        int index = str.indexOf(":");
                        if (index == -1)
                            continue;
                        String s = str.substring(index + 1).trim();

                        int r = Integer.parseInt(s.substring(0, 3));
                        int g = Integer.parseInt(s.substring(3, 6));
                        int b = Integer.parseInt(s.substring(6, 9));
                        paint.setColor(Color.rgb(r, g, b));

                    }
                }
            }
        }
        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        return paint;
    }

    /**
     * Dibujar eje X
     *
     * @param canvas Objeto de lienzo
     */
    private void drawXAxisLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawLine(rect.left, rect.bottom, rect.right, rect.bottom, paint);
    }

    /**
     * Dibujar eje Y
     *
     * @param canvas Objeto de lienzo
     */
    private void drawYAxisLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawLine(rect.left, rect.bottom, rect.left, rect.top, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    public View getView() {
        return this;
    }
}

package org.egc.commons.raster;
import org.egc.commons.exception.RasterReadException;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.osr.SpatialReference;
/**
 * Created by lp on 2017/4/25.
 */
public class GeoInfoExtraction {
    RasterInfo rasterInfo = new RasterInfo();
    //读取栅格的空间坐标系编码srid、空间分辨率、no_data、包络面范围（上下左右）
    public RasterInfo readMetaData(String rasterPath){

        gdal.AllRegister();
        Dataset dataset = gdal.Open(rasterPath, gdalconstConstants.GA_ReadOnly);
        if (dataset == null){
            throw new RasterReadException("read raster error");
        }
        Driver driver = dataset.GetDriver();
        Integer iWidth = dataset.GetRasterXSize();
        Integer iHeight = dataset.GetRasterYSize();

        String  spatialReference = dataset.GetProjectionRef();
        SpatialReference spatialReference1 = new SpatialReference(spatialReference);
        Integer srid = Integer.parseInt(spatialReference1.GetAuthorityCode(null));

        Band band = dataset.GetRasterBand(1);
        Double[] noDataval = new Double[2];
        band.GetNoDataValue(noDataval);

        double dGeotransform[]  = dataset.GetGeoTransform();
        double left = dGeotransform[0];
        double top = dGeotransform[3];
        double right = left + dGeotransform[1] * iWidth + dGeotransform[2] * iHeight;
        double bottom = top + dGeotransform[4] * iWidth + dGeotransform[5] * iHeight;
        double pixelSize = dGeotransform[1];
        rasterInfo.setDown(bottom);
        rasterInfo.setTop(top);
        rasterInfo.setLeft(left);
        rasterInfo.setRight(right);
        rasterInfo.setNoData(noDataval[0]);
        rasterInfo.setSrid(srid);
        rasterInfo.setPixelSize(pixelSize);
        return rasterInfo;
    }
}
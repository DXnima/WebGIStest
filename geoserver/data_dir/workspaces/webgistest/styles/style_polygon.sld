<?xml version="1.0" encoding="GBK"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
    <sld:UserLayer>
        <sld:LayerFeatureConstraints>
            <sld:FeatureTypeConstraint/>
        </sld:LayerFeatureConstraints>
        <sld:UserStyle>
            <sld:Name>Default Styler</sld:Name>
            <sld:FeatureTypeStyle>
                <sld:Name>name</sld:Name>
                <sld:FeatureTypeName>Feature</sld:FeatureTypeName>
                <sld:Rule>
                    <sld:Name>New rule</sld:Name>
                    <sld:MaxScaleDenominator>5.59E8</sld:MaxScaleDenominator>
                    <sld:PolygonSymbolizer>
                        <sld:Geometry>
                            <ogc:Function name="offset">
                                <ogc:PropertyName>geom</ogc:PropertyName>
                                <ogc:Literal>0.0</ogc:Literal>
                                <ogc:Literal>0.0</ogc:Literal>
                            </ogc:Function>
                        </sld:Geometry>
                        <sld:Fill>
                            <sld:CssParameter name="fill">#ff0000</sld:CssParameter>
                            <sld:CssParameter name="fill-opacity">0.5</sld:CssParameter>
                        </sld:Fill>
                        <sld:Stroke/>
                    </sld:PolygonSymbolizer>
                </sld:Rule>
            </sld:FeatureTypeStyle>
        </sld:UserStyle>
    </sld:UserLayer>
</sld:StyledLayerDescriptor>

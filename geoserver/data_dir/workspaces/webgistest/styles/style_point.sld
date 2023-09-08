<?xml version="1.0" encoding="GBK"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
    <sld:UserLayer>
        <sld:LayerFeatureConstraints>
            <sld:FeatureTypeConstraint/>
        </sld:LayerFeatureConstraints>
        <sld:UserStyle>
            <sld:Name>Default Styler</sld:Name>
            <sld:Title>dark yellow square point style</sld:Title>
            <sld:FeatureTypeStyle>
                <sld:Name>name</sld:Name>
                <sld:FeatureTypeName>Feature</sld:FeatureTypeName>
                <sld:Rule>
                    <sld:Title>dark yellow point</sld:Title>
                    <sld:MaxScaleDenominator>5.59E8</sld:MaxScaleDenominator>
                    <sld:PointSymbolizer>
                        <sld:Geometry>
                            <ogc:Function name="offset">
                                <ogc:PropertyName>geom</ogc:PropertyName>
                                <ogc:Literal>0.0</ogc:Literal>
                                <ogc:Literal>0.0</ogc:Literal>
                            </ogc:Function>
                        </sld:Geometry>
                        <sld:Graphic>
                            <sld:Mark>
                                <sld:WellKnownName>circle</sld:WellKnownName>
                                <sld:Fill>
                                    <sld:CssParameter name="fill">#FF0000</sld:CssParameter>
                                </sld:Fill>
                                <sld:Stroke>
                                    <sld:CssParameter name="stroke-opacity">0</sld:CssParameter>
                                    <sld:CssParameter name="stroke-width">0</sld:CssParameter>
                                </sld:Stroke>
                            </sld:Mark>
                            <sld:Size>10</sld:Size>
                        </sld:Graphic>
                    </sld:PointSymbolizer>
                </sld:Rule>
            </sld:FeatureTypeStyle>
        </sld:UserStyle>
    </sld:UserLayer>
</sld:StyledLayerDescriptor>


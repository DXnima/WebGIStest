package com.example.webgistest.test.ecql;

import org.geotools.filter.text.commons.AbstractCompilerFactory;
import org.geotools.filter.text.commons.ICompiler;
import org.geotools.filter.text.ecql.ECQLCompiler;
import org.opengis.filter.FilterFactory;

/**
 * Provides the implementation of {@link ECQLCompiler}
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
final class ECQLCompilerFactory extends AbstractCompilerFactory {

    /** Creates an instance of {@link ECQLCompiler} */
    @Override
    protected ICompiler createCompiler(final String predicate, final FilterFactory filterFactory) {

        return new ECQLCompiler(predicate, filterFactory);
    }
}

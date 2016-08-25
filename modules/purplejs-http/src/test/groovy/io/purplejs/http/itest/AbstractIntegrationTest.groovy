package io.purplejs.http.itest

import com.google.common.base.Charsets
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import io.purplejs.core.Engine
import io.purplejs.core.EngineBinder
import io.purplejs.core.EngineBuilder
import io.purplejs.core.mock.MockResource
import io.purplejs.core.mock.MockResourceLoader
import io.purplejs.core.resource.ResourceLoaderBuilder
import io.purplejs.core.resource.ResourcePath
import io.purplejs.http.Request
import io.purplejs.http.RequestBuilder
import io.purplejs.http.Response
import io.purplejs.http.handler.HttpHandlerFactory
import io.purplejs.http.websocket.WebSocketEvent
import spock.lang.Specification

abstract class AbstractIntegrationTest
    extends Specification
{
    protected Engine engine;

    private MockResourceLoader resourceLoader;

    private HttpHandlerFactory handlerFactory;

    protected RequestBuilder requestBuilder;

    public void setup()
    {
        final EngineBuilder builder = EngineBuilder.newBuilder();
        configureEngine( builder );

        this.engine = builder.build();
        this.handlerFactory = this.engine.getInstance( HttpHandlerFactory.class );

        this.requestBuilder = RequestBuilder.newBuilder();
    }

    public final void cleanup()
    {
        this.engine.dispose();
    }

    private void configureEngine( final EngineBuilder builder )
    {
        this.resourceLoader = new MockResourceLoader();

        final ResourceLoaderBuilder resourceLoaderBuilder = ResourceLoaderBuilder.newBuilder();
        resourceLoaderBuilder.from( getClass().getClassLoader() );
        resourceLoaderBuilder.add( this.resourceLoader );

        builder.resourceLoader( resourceLoaderBuilder.build() );
        builder.module { binder -> configureModule( binder ) };
    }

    protected void configureModule( final EngineBinder binder )
    {
        binder.globalVariable( "t", this );
    }

    protected final MockResource file( final String path, final String content )
    {
        return this.resourceLoader.addResource( path, content.trim() );
    }

    protected final Response serve( final String path, final Request request )
    {
        return serve( ResourcePath.from( path ), request );
    }

    protected final Response serve( final ResourcePath path, final Request request )
    {
        return this.handlerFactory.newHandler( path ).serve( request );
    }

    public void assertEquals( final Object expected, final Object actual )
    {
        assert expected == actual;
    }

    protected final void script( final String content )
    {
        file( '/test.js', content );
    }

    protected final boolean handleEvent( final WebSocketEvent event )
    {
        return this.handlerFactory.newHandler( ResourcePath.from( '/test.js' ) ).handleEvent( event );
    }

    protected final Response serve()
    {
        return serve( '/test.js', this.requestBuilder.build() );
    }

    protected final static String toStringBody( final Response response )
    {
        return response.body.asCharSource( Charsets.UTF_8 ).read();
    }

    protected final static String prettifyJson( final String json )
    {
        final Gson gson = new GsonBuilder().
            setPrettyPrinting().
            create();

        return gson.toJson( gson.fromJson( json.trim(), JsonElement.class ) );
    }
}

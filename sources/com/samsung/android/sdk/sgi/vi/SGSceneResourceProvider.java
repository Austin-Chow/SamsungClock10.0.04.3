package com.samsung.android.sdk.sgi.vi;

import com.samsung.android.sdk.sgi.render.SGTextureProperty;
import java.io.IOException;

public interface SGSceneResourceProvider {
    void clear();

    Object get(String str);

    SGDataReader getStream(String str);

    SGTextureProperty getTexture(String str) throws IOException;

    void put(String str, Object obj);
}

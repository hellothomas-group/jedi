package com.hellothomas.jedi.admin.common.utils;


import com.hellothomas.jedi.admin.domain.Executor;

public class ReleaseKeyGenerator extends UniqueKeyGenerator {


    /**
     * Generate the release key in the format: timestamp+appId+cluster+namespace+hash(ipAsInt+counter)
     *
     * @param executor the namespace of the release
     * @return the unique release key
     */
    public static String generateReleaseKey(Executor executor) {
        return generate(executor.getNamespaceName(), executor.getAppId(), executor.getExecutorName());
    }
}

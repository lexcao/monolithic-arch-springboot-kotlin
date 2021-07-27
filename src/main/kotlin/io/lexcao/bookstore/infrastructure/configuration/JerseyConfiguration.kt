package io.lexcao.bookstore.infrastructure.configuration

import org.glassfish.jersey.server.ResourceConfig
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.util.ClassUtils
import javax.ws.rs.ApplicationPath
import javax.ws.rs.Path
import javax.ws.rs.ext.Provider

/**
 * Jersey服务器配置
 * <p>
 * 使用Jersey来提供对JAX-RS（JSR 370：Java API for Restful Web Services）的支持
 * 这里设置了所有服务的前缀路径“restful”和restful服务资源的包路径
 **/
@Configuration
@ApplicationPath("/restful")
class JerseyConfiguration : ResourceConfig() {

    init {
        scanPackages("io.lexcao.bookstore.resource")
        scanPackages("io.lexcao.bookstore.infrastructure.jaxrs")
    }

    /**
     * Jersey的packages()方法在Jar形式运行下有问题，这里修理一下
     */
    private fun scanPackages(scanPackage: String) {
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(AnnotationTypeFilter(Path::class.java))
        scanner.addIncludeFilter(AnnotationTypeFilter(Provider::class.java))
        this.registerClasses(scanner.findCandidateComponents(scanPackage)
            .mapNotNull { beanDefinition: BeanDefinition ->
                ClassUtils.resolveClassName(beanDefinition.beanClassName!!, this.classLoader)
            }.toSet()
        )
    }
}

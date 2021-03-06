/*
 * Copyright 2017-2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.inject.qualifiers.replaces

import io.micronaut.context.ApplicationContext
import io.micronaut.context.BeanContext
import io.micronaut.context.DefaultBeanContext
import spock.lang.Specification
/**
 * Created by graemerocher on 26/05/2017.
 */
class ReplacesSpec extends Specification {

//    @Ignore
    void "test that a bean can be marked to replace another bean"() {
        given:
        BeanContext context = new DefaultBeanContext().start()

        when:"A bean has a dependency on an interface with multiple impls"
        B b = context.getBean(B)

        then:"The impl that replaces the other impl is the only one present"
        b.all.size() == 1
        !b.all.any() { it instanceof A1 }
        b.all.any() { it instanceof A2 }
        b.a instanceof A2
    }

    void "test that introduction advice can be replaced"() {
        given:
        def ctx = ApplicationContext.run()

        when:
        IntroductionOperations ops = ctx.getBean(IntroductionOperations)

        then:
        ops instanceof IntroductionReplacement
        ctx.getBeansOfType(IntroductionOperations).size() == 1


        cleanup:
        ctx.close()
    }

    void "test that classes with around advice can be replaced"() {
        given:
        def ctx = ApplicationContext.run()

        when:
        AroundOps ops = ctx.getBean(AroundOps)

        then:
        ops instanceof AroundReplacement
        ctx.getBeansOfType(AroundOps).size() == 1
        ctx.getBeansOfType(AroundOps).iterator().next()  == ops

        cleanup:
        ctx.close()
    }
}

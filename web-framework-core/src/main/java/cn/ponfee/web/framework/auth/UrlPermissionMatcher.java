package cn.ponfee.web.framework.auth;

import static code.ponfee.commons.util.SpringContextHolder.getBean;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import cn.ponfee.web.framework.model.Permit;
import cn.ponfee.web.framework.service.IPermitService;
import cn.ponfee.web.framework.service.IUserService;
import code.ponfee.commons.tree.FlatNode;

/**
 * Url permissions
 * 
 * @author Ponfee
 */
public final class UrlPermissionMatcher {

    public static final AntPathMatcher MATCHER = new AntPathMatcher();

    /** The system of permission urls of t_permit */
    private volatile static Map<String, FlatNode<String, Permit>> permissions;

    /** Spring mvc url mappings of annotation RequestMapping */
    private static List<String> mappings;

    public static boolean hasNotPermission(String url, long userId) {
        return !hasPermission(url, userId);
    }

    /**
     * 判断用户是否有该url的访问权限
     * 
     * @param url      the access url
     * @param userId   the username
     * @return {@code true} has permit
     */
    public static boolean hasPermission(String url, long userId) {
        Collection<String> userPermits = getBean(IUserService.class).permitList(userId).getData();
        if (userPermits == null) {
            userPermits = Collections.emptyList();
        }

        boolean required = false; // 是否需要权限控制
        boolean hasPermit = false; // 是否有权限
        for (Iterator<Entry<String, FlatNode<String, Permit>>> i = permissions().entrySet().iterator(); i.hasNext();) {
            FlatNode<String, Permit> node = i.next().getValue();
            Permit permit = node.getAttach();
            if (StringUtils.isNotBlank(permit.getPermitUrl()) && MATCHER.match(permit.getPermitUrl(), url)) {
                required = true;
                if (node.isAvailable() && userPermits.contains(node.getNid())) {
                    hasPermit = true;
                    break;
                }
            }
        }

        return !required || hasPermit;
    }

    public static void invalidPermits() {
        permissions = null;
    }

    /**
     * Check is the spring mvc controller url mapping
     * 
     * @param url the user request url address
     * @return {@code true} is the spring mvc url mapping
     */
    public static boolean isMapping(String url) {
        List<String> urls = mappings();
        if (urls == null) {
            return false;
        }

        for (String perm : urls) {
            if (MATCHER.match(perm, url)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNotMapping(String url) {
        return !isMapping(url);
    }

    /**
     * Clears the permissions when changed
     */
    public static void clearPermissions() {
        permissions = null;
    }

    /**
     * Returns the spring mvc request mapping urls
     * 
     * @return spring mvc request mapping urls set
     * @see org.springframework.web.bind.annotation.RequestMapping
     */
    private static List<String> mappings() {
        List<String> urls = mappings;
        if (urls == null) {
            synchronized (UrlPermissionMatcher.class) {
                if ((urls = mappings) == null) {
                    Set<String> set = getBean(RequestMappingHandlerMapping.class).getHandlerMethods()
                    .entrySet().stream().map(
                        e -> e.getKey().getPatternsCondition().getPatterns()
                    ).flatMap(
                        s -> s.stream()
                    ).collect(
                        Collectors.toSet()
                    );
                    mappings = urls = ImmutableList.copyOf(set);
                }
            }
        }
        return urls;
    }

    /**
     * Returns all the permissions data of dabatase table t_permit
     * 
     * @return Map<String, PermitFlat>
     */
    private static Map<String, FlatNode<String, Permit>> permissions() {
        Map<String, FlatNode<String, Permit>> permits = permissions;
        if (permits == null) {
            synchronized (UrlPermissionMatcher.class) {
                if ((permits = permissions) == null) {
                    List<FlatNode<String, Permit>> list = getBean(IPermitService.class).flatsAll().getData();
                    permissions = permits = 
                    CollectionUtils.isEmpty(list) 
                    ? Collections.emptyMap() 
                    : ImmutableMap.copyOf(
                        list.subList(1, list.size()).stream().collect(
                            Collectors.toMap(FlatNode::getNid, Function.identity())
                        )
                    ); // remove root
                }
            }
        }
        return permits;
    }

}

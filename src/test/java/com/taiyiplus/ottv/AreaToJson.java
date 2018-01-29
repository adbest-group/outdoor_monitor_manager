package com.taiyiplus.ottv;

public class AreaToJson extends BaseTest {

	// @Autowired
	// private SysZoneMapper mapper;
	//
	// @Test
	// public void zone2Json() {
	// int i = 0;
	// List<SysZone> result = Lists.newArrayList();
	// while (true) {
	// List<SysZone> r = mapper.selectList(new RowBounds(i * 1000, 1000));
	// if (CollectionUtil.isEmpty(r)) {
	// break;
	// }
	//
	// result.addAll(r);
	// i++;
	// }
	//
	// Map<Integer, List<B>> map = Maps.newHashMap();
	// Map<Integer, SysZone> maps = Maps.newHashMap();
	// for (SysZone sz : result) {
	// if (sz.getRegionType() == 2) {
	// List<B> a = map.get(sz.getParentId());
	// if (CollectionUtil.isNotEmpty(a)) {
	// map.get(sz.getParentId()).add(new B(sz.getId(), sz.getDicvalue()));
	// } else {
	// List<B> b = Lists.newArrayList();
	// b.add(new B(sz.getId(), sz.getDicvalue()));
	// map.put(sz.getParentId(), b);
	// }
	// } else {
	// maps.put(sz.getId(), sz);
	// }
	// }
	//
	// B[] barray = new B[map.size()];
	// int j = 0;
	// for (Entry<Integer, List<B>> en : map.entrySet()) {
	// B v = new B();
	// SysZone s = maps.get(en.getKey());
	// v.setId(s.getId());
	// v.setName(s.getDicvalue());
	// v.setChild(en.getValue());
	// barray[j] = v;
	// j++;
	// }
	//
	// System.out.println(GsonUtil.GsonString(barray));
	// }
	//
	// class B {
	// public B() {
	// }
	//
	// public B(int id, String name) {
	// this.id = id;
	// this.name = name;
	// }
	//
	// private int id;
	// private String name;
	// private List<B> child;
	//
	// public int getId() {
	// return id;
	// }
	//
	// public void setId(int id) {
	// this.id = id;
	// }
	//
	// public String getName() {
	// return name;
	// }
	//
	// public void setName(String name) {
	// this.name = name;
	// }
	//
	// public List<B> getChild() {
	// return child;
	// }
	//
	// public void setChild(List<B> child) {
	// this.child = child;
	// }
	//
	// }
}

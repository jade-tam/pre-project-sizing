import { useMutation, useQueryClient } from "@tanstack/react-query";
import { userApi } from "../api/user.api";
import { userQueryKeys } from "../queries/query-keys";

export function usePatchCurrentUserMutation() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: userApi.patchCurrentUser,

    onSuccess: (response) => {
      const { data: user } = response;
      queryClient.setQueryData(userQueryKeys.current(), user);
      queryClient.invalidateQueries({ queryKey: userQueryKeys.current() });
    },
  });
}
